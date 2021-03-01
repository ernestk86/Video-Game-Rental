package com.vgrental.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.vgrental.exceptions.RegisterUserFailedException;
import com.vgrental.exceptions.UpdateUserFailedException;
import com.vgrental.models.Game;
import com.vgrental.repositories.IGameDAO;
import com.vgrental.repositories.IReviewDAO;
import com.vgrental.repositories.ReviewDAO;
import com.vgrental.repositories.GameDAO;

public class GameService implements IGameService{
	private IGameDAO gameDAO = new GameDAO();
	private IReviewDAO reviewDAO = new ReviewDAO();
	
	private static final Logger log = LoggerFactory.getLogger(GameService.class);

	public List<Game> search(String s) {
		List<Game> searchResults = gameDAO.search(s); // Grab search results
		
		for(Game g : searchResults) {
			// Calculate rating averages and assign them to each game
			g.setAvgRating(reviewDAO.getGameAvg(g.getId()));
		}
		
		// Logging
		log.info("Search happened");
		MDC.put("searchTerm", s);
		return searchResults;
	}
	
	public List<Game> findAll() {
		return gameDAO.findAll();
	}
	
	public Game findById(int id) {
		Game g = gameDAO.findByGameId(id); // Grab game
		g.setAvgRating(reviewDAO.getGameAvg(id)); // Calculate average rating
		return g;
	}
	
	public Game addGame(Game g) {
		// Logging
		MDC.put("event", "Register");		
		log.info("Registering new game");
		
		// Game already exists
		if(g.getId() != 0) {			
			throw new RegisterUserFailedException("Received Game object did not have ID = 0");
		}
		
		int generatedId = gameDAO.insert(g); // Add game and get assigned gameId
		
		// Make sure there are no errors
		if(generatedId != -1 && generatedId != g.getId()) {			
			g.setId(generatedId);
		} else {
			throw new RegisterUserFailedException("Failed to insert the game");
		}
		
		// Logging
		MDC.put("gameId", Integer.toString(g.getId()));
		log.info("Successfully registered game");
		
		return g;
	}
	
	public boolean update(Game g) {
		// Make sure game exists
		if(g.getId() == 0) {			
			throw new UpdateUserFailedException("Received User object where ID = 0, game does not exist");
		}
		
		// Make sure game updates successfully
		if(!gameDAO.update(g)) {
			throw new UpdateUserFailedException("Failed to update the User record");
		}
		
		return true;
	}
	
	public boolean deleteGame(int id) {
		if(this.findById(id) == null) return false; // Make sure game exists
		return gameDAO.delete(id); // Delete game
	}
	
	public void toggleAvailable(Game g) {
		g.setAvailable(!g.isAvailable());
		this.update(g);
	}
}