package com.vgrental.repositories;

import java.util.List;

import com.vgrental.models.Game;

public interface IGameDAO {
	public List<Game> findAll(); // Returns a list of every game
	public Game findByGameId(int gameId); // Returns game
	public List<Game> search(String gameName); // Returns a list of games from a search term
	public int insert(Game g); // Returns the generated primary key
	public boolean update(Game g); // Returns true if operation is successful
	public boolean delete(int gameId); // Returns true if operation is successful
}
