package com.vgrental.services;

import java.util.List;

import com.vgrental.models.Game;

public interface IGameService {
	public List<Game> search(String s); // Returns a list of full game data, including average rating score
	public List<Game> findAll(); // Returns a list of full game data for every game
	public Game findById(int id); // Returns game, insert game id
	public Game addGame(Game g); // Returns game with full game info, including id, insert game with id = 0
	public boolean update(Game g); // Returns true if operation is successful, insert game with ALREADY updated data
	public boolean deleteGame(int id); // Returns true if operation is successful
	public void toggleAvailable(Game g); // Insert game to toggle it's availability
}
