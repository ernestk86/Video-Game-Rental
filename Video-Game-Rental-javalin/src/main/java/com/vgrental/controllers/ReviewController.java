package com.vgrental.controllers;

import org.slf4j.MDC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vgrental.models.Review;
import com.vgrental.services.ReviewService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ReviewController implements Controller {
	
	private ReviewService reviewService = new ReviewService();
	private ObjectMapper mapper = new ObjectMapper();
	
	private static final String USERNAME = "username";
	private static final String ADMIN = "admin";
	private static final String UNAUTHORIZED = "Unauthorized";
	private static final String ID = "id";

	private Handler findAll = ctx -> {
		ctx.json(reviewService.findAll()).status(200);
		MDC.clear(); // Logging
	};

	private Handler addReview = ctx -> {
		// Grab review data from supplied JSON
		String bodyJSON = ctx.body();
		Review r = mapper.readValue(bodyJSON, Review.class);
		String username = ctx.pathParam(USERNAME); // Grab supplied username
		
		// Post review if user is authenticated
		if(Controller.authenticate(ctx, username)) {
			r = reviewService.addReview(r);
			ctx.json(r).status(201); // Return review data
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	private Handler deleteReview = ctx -> {
		// Grab review from supplied id
		String idString = ctx.pathParam(ID);
		int id = Integer.parseInt(idString);
		Review r = reviewService.findReview(id);
		
		// Check if admin is logged in to delete review
		if(Controller.authenticate(ctx, ADMIN)) {
			if(reviewService.deleteReview(r)) {
				ctx.result("Review deleted").status(200);
			} else {
				ctx.result("Review not deleted").status(500);
			}
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	@Override
	public void addRoutes(Javalin app) {
		app.get("/reviews", this.findAll);
		
		app.post("/reviews/:username", this.addReview);
		
		app.delete("/reviews/:id", this.deleteReview);
	}

}
