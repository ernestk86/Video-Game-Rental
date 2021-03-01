package com.vgrental.controllers;

import java.util.Stack;

import org.json.JSONObject;
import org.slf4j.MDC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vgrental.models.Game;
import com.vgrental.services.GameService;
import com.vgrental.services.ReviewService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class GameController implements Controller {
	
	private GameService gameService = new GameService();
	private ReviewService reviewService = new ReviewService();
	private ObjectMapper mapper = new ObjectMapper();
	
	private static final String USERNAME = "username";
	private static final String ADMIN = "admin";
	private static final String UNAUTHORIZED = "Unauthorized";
	private static final String ID = "id";
	private static final String GAMES = "/games";
	
	private Handler getAllGames = ctx -> {
		ctx.json(gameService.findAll()).status(200);
		MDC.clear(); // Logging
	};
	
	private Handler searchForGame = ctx -> {
		// Grab username from supplied JSON for authentication
		String stringJSON = ctx.body();
		JSONObject obj = new JSONObject(stringJSON);
		String username = obj.getString(USERNAME);
		
		// Make sure user is logged in to search
		if(Controller.authenticate(ctx, username)) {
			String name = ctx.pathParam("name");
			ctx.json(gameService.search(name)).status(200);
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};

	private Handler getSingleGame = ctx -> {
		// Grab username from supplied JSON for authentication
		String stringJSON = ctx.body();
		JSONObject obj = new JSONObject(stringJSON);
		String username = obj.getString(USERNAME);
		
		// Make sure user is logged in
		if(Controller.authenticate(ctx, username)) {
			// Grab supplied game id
			String idString = ctx.pathParam(ID);
			int id = Integer.parseInt(idString);
			
			// Prepare data to send back
			Game g = gameService.findById(id); // Grab game info
			Stack<Object> gameData = reviewService.getReviews(id); // Grab reviews
			gameData.push(g); // Add game info 
			
			ctx.json(gameData).status(200);
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};

	private Handler addGame = ctx -> {
		// Make sure admin is logged in
		if(Controller.authenticate(ctx, ADMIN)) {
			// Grab data game to insert from JSON
			String bodyJSON = ctx.body();
			Game g = mapper.readValue(bodyJSON, Game.class);
			
			g = gameService.addGame(g); // Insert game and get back game with updated ID
			
			ctx.json(g).status(201);
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	private Handler updateGame = ctx -> {
		// Grab game data from supplied JSON
		String bodyJSON = ctx.body();
		Game g = mapper.readValue(bodyJSON, Game.class);
		
		// Make sure admin is logged in
		if(Controller.authenticate(ctx, ADMIN)) {
			if(gameService.update(g)) { // Update game
				ctx.json(g).status(200);
			} else {
				ctx.result("Unable to update game").status(500);
			}
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
		
		MDC.clear(); // Logging
	};
	
	private Handler deleteGame = ctx -> {
		// Grab supplied game id
		String idString = ctx.pathParam(ID);
		int id = Integer.parseInt(idString);
		
		// Make sure admin is logged in
		if(Controller.authenticate(ctx, ADMIN)) {
			if(gameService.deleteGame(id)) { // Delete game
				ctx.result("Game deleted").status(200);
			} else {
				ctx.result("Game not deleted").status(500);
			}
		} else {
			ctx.status(401).result(UNAUTHORIZED);
		}
	
		MDC.clear(); // Logging
	};
	
	@Override
	public void addRoutes(Javalin app) {
		app.get(GAMES, this.getAllGames);

		app.get("/games/search/:name", this.searchForGame);
		
		app.get("/games/:id", this.getSingleGame);
		
		app.post(GAMES, this.addGame);
		
		app.put(GAMES, this.updateGame);
		
		app.delete("/games/:id", this.deleteGame);
	}

}
