package com.vgrental.controllers;

import java.util.Stack;

import org.slf4j.MDC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vgrental.models.Rental;
import com.vgrental.services.RentalService;
import com.vgrental.services.UserService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class RentalController implements Controller {
	
	private RentalService rentalService = new RentalService();
	private UserService userService = new UserService();
	private ObjectMapper mapper = new ObjectMapper();
	
	private static final String ADMIN = "admin";
	private static final String UNAUTHORIZED = "Unauthorized";

	private Handler findUserRentals = ctx -> {
		String idString = ctx.pathParam("username");
		Stack<Object> rentals = rentalService.findUserRentalGameData(userService.findByUsername(idString));
		ctx.json(rentals).status(200);
		MDC.clear(); // Logging
	};
	
	private Handler rentGame = ctx -> {
		// Grab rental from supplied JSON
		String bodyJSON = ctx.body();
		Rental r = mapper.readValue(bodyJSON, Rental.class);
		// Grab username for authentication purposes
		String username = userService.findById(r.getUserId()).getUsername();
		
		// Make sure user is logged in
		if(Controller.authenticate(ctx, username)) {
			if(!rentalService.rentGame(r)) { // Rent game
				ctx.result("Game is unavailable").status(406);
			} else {
				ctx.result("Game successfully rented!").status(201);
			}
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	private Handler overdueRental = ctx -> {
		// Grab rental information from supplied JSON
		String bodyJSON = ctx.body();
		Rental temp = mapper.readValue(bodyJSON, Rental.class);
		Rental r = rentalService.findRental(temp.getUserId(), temp.getGameId());
		
		// Make sure admin is logged in
		if(Controller.authenticate(ctx, ADMIN)) {
			if(rentalService.toggleOverdue(r)) { // Toggle overdue true or false
				if(r.isOverDue()) {
					ctx.result("Rental now overdue").status(200); // Sends email to user
				} else {
					ctx.result("Rental not overdue").status(200);
				}
			} else {
				ctx.result("Rental overdue not toggled").status(500);
			}
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	private Handler changeDate = ctx -> {
		// Grab rental from supplied JSON
		String bodyJSON = ctx.body();
		Rental temp = mapper.readValue(bodyJSON, Rental.class);
		Rental r = rentalService.findRental(temp.getUserId(), temp.getGameId());
		
		// Make sure admin is logged in
		if(Controller.authenticate(ctx, ADMIN)) {
			if(rentalService.changeDueDate(r, temp.getDueDate())) { // Change rental due date
				ctx.result("Rental due date updated").status(200);
			} else {
				ctx.result("Rental due date not updated").status(500);
			}
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	private Handler returnGame = ctx -> {
		// Grab rental from supplied JSON
		String bodyJSON = ctx.body();
		Rental r = mapper.readValue(bodyJSON, Rental.class);
		// Grab username for authentication purposes
		String username = userService.findById(r.getUserId()).getUsername();
		
		// Make sure user is logged in
		if(Controller.authenticate(ctx, username)) {
			if(rentalService.returnGame(r)) { // Return game
				ctx.status(200).result("Game has been returned");
			} else {
				ctx.status(500).result("Game has not been returned, for whatever reason");
			}
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	@Override
	public void addRoutes(Javalin app) {
		app.get("/rent/:username", this.findUserRentals);
		
		app.post("/rent", this.rentGame);
		
		app.put("/rent/overdue", this.overdueRental);
		
		app.put("/rent/changeDate", this.changeDate);
		
		app.delete("/rent", this.returnGame);

	}

}
