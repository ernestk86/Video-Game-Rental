package com.vgrental.controllers;

import java.util.Stack;

import org.json.JSONObject;
import org.slf4j.MDC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vgrental.models.*;
import com.vgrental.services.RentalService;
import com.vgrental.services.UserService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class UserController implements Controller {
	
	private UserService userService = new UserService();
	private RentalService rentalService = new RentalService();
	private ObjectMapper mapper = new ObjectMapper();
	
	// OPTIMIZATION Defined constants
	private static final String USERNAME = "username";
	private static final String UNAUTHORIZED = "Unauthorized";
	private static final String ID = "id";
	private static final String USERS = "/users";

	private Handler getAllUsers = ctx -> {
		ctx.json(userService.findAll()).status(200);	
		MDC.clear(); // Logging
	};
	
	private Handler getSingleUser = ctx -> {
		String idString = ctx.pathParam(ID); // Grab path parameter
		
		// Check if a user_id was entered or a username
		User u;
		if(idString.charAt(0) >= 48 && idString.charAt(0) <= 57) {
			int id = Integer.parseInt(idString);
			u = userService.findById(id);
		} else {
			u = userService.findByUsername(idString);
		}
		
		// Grab all rentals for user
		Stack<Object> userData = rentalService.findUserRentalGameData(u);
		userData.push(u);
		
		// Make sure user is successfully logged in
		if(Controller.authenticate(ctx, u.getUsername())) {
			ctx.json(userData);
			ctx.status(200);
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	private Handler authenticateUser = ctx -> {
		// Grab information from JSON
		// Cannot use mapper because we don't need every field for User
		// just the username and password
		String stringJSON = ctx.body();
		JSONObject obj = new JSONObject(stringJSON);
		String username = obj.getString(USERNAME);
		String password = obj.getString("password");
		
		// Grab user data from database
		User u = userService.findByUsername(username);
		
		// Make sure username and password match
		if(username.equals(u.getUsername()) && (password.equals(u.getPassword()))) {
			ctx.sessionAttribute(username, true); // Save authorization in session
			ctx.status(200).result("Authorized");
			ctx.redirect("/users/" + username);  // Redirect to user's profile page
		} else {
			ctx.sessionAttribute(username, false);
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	private Handler createUser = ctx -> {
		// Jackson databind is used to map JSON to class
		String bodyJSON = ctx.body();
		User u = mapper.readValue(bodyJSON, User.class);
		
		u = userService.register(u); // Register user
		
		ctx.sessionAttribute(u.getUsername(), true); // Save user as authorized in session
		ctx.json(u).status(201);
		
		MDC.clear(); // Logging
	};
	
	private Handler logout = ctx -> {
		String username = ctx.pathParam(USERNAME); // Grab username to logout
		ctx.sessionAttribute(username, false); // Save unauthorization in session
		ctx.result("Successfully logged out").status(200);
		MDC.clear(); // Logging
	};
	
	private Handler updateUser = ctx -> {
		// Grab data from supplied JSON
		String bodyJSON = ctx.body();
		User u = mapper.readValue(bodyJSON, User.class);
		
		// Make sure user is successfully logged in
		if(Controller.authenticate(ctx, u.getUsername())) {
			if(userService.update(u)) {
				ctx.json(u).status(200); // Send back the registered User with the updated ID
			} else {
				ctx.result("Unable to update user").status(500);
			}
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	private Handler deleteUser = ctx -> {
		// Get username from given id
		String idString = ctx.pathParam(ID);
		int id = Integer.parseInt(idString);
		User u = userService.findById(id);
		String username = u.getUsername();
		
		// Make sure user is successfully logged in and has no rentals out
		if(Controller.authenticate(ctx, username) && (rentalService.findUserRentalGameData(u).isEmpty())) {
			//Delete User
			if(userService.deleteUser(id)) {
				ctx.result("User deleted");
				ctx.status(200);
			} else {
				ctx.result("User not deleted");
				ctx.status(500);
			}
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};

	
	public void addRoutes(Javalin app) {
		app.get(USERS, this.getAllUsers);
		
		app.get("/users/:id", this.getSingleUser);
		
		app.post("/users/authenticate", this.authenticateUser);
		
		app.post(USERS, this.createUser);
		
		app.get("/users/logout/:username", this.logout);
		
		app.put(USERS, this.updateUser);
		
		app.delete("/users/:id", this.deleteUser);
	}
}

