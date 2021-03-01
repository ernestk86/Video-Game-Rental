package com.vgrental.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.vgrental.models.Game;
import com.vgrental.models.CONSOLES;
import com.vgrental.models.GENRE;
import com.vgrental.util.ConnectionUtil;

public class GameDAO implements IGameDAO {
	
	private static final Logger log = LoggerFactory.getLogger(GameDAO.class);

	@Override
	public List<Game> findAll() {
		List<Game> allGames = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			
			// Get results from DB
			Statement stmt = conn.createStatement();			
			String sql = "SELECT * FROM vg_rentals.games";			
			ResultSet rs = stmt.executeQuery(sql); // Send the Statement to the DB
			stmt.close();
			
			// Iterate through the response, one row at a time
			while(rs.next()) {				
				int id = rs.getInt("id");
				String name = rs.getString("name");
				GENRE genre = GENRE.valueOf(rs.getString("genre"));
				CONSOLES console = CONSOLES.valueOf(rs.getString("console"));
				String publisher = rs.getString("publisher");
				String developer = rs.getString("developer");
				int yearReleased = rs.getInt("year_released");
				boolean multiplayer = rs.getBoolean("multiplayer");
				
				// Add game to list
				allGames.add(new Game(id, name, genre, console, publisher, developer, yearReleased, multiplayer));
			}
			
		} catch(SQLException e) {
			log.error("We failed to retrieve all games", e); // Logging
			return new ArrayList<>();
		}
		
		// Logging
		log.info("Retrieved every game");
		MDC.put("allGames", "1");
		
		return allGames;
	}

	@Override
	public Game findByGameId(int gameId) {
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Get query ready
			String sql = "SELECT * FROM vg_rentals.games WHERE id = ?";		
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, gameId); // Inserts value into ? in sql string
			
			ResultSet rs = stmt.executeQuery(); // Execute query
			stmt.close();
			rs.next(); // Pointer starts before first record in returned result set
			
			// Set game data
			int id = rs.getInt("id");
			String name = rs.getString("name");
			GENRE genre = GENRE.valueOf(rs.getString("genre"));
			CONSOLES console = CONSOLES.valueOf(rs.getString("console"));
			String publisher = rs.getString("publisher");
			String developer = rs.getString("developer");
			int yearReleased = rs.getInt("year_released");
			boolean multiplayer = rs.getBoolean("multiplayer");
			boolean available = rs.getBoolean("available");
			
			// Logging
			log.info("Found game");
			MDC.put("foundGameId", Integer.toString(id));
			
			return new Game(id, name, genre, console, publisher, developer, yearReleased, multiplayer, available);
			
		} catch(SQLException e) {
			log.error("We failed to retrieve game", e); // Logging
			return null;
		}
	}

	public List<Game> search(String gameName) {
		List<Game> allGames = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "SELECT * FROM vg_rentals.games WHERE name LIKE ?";	
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + gameName + "%"); // Inserts value into ? in sql string
			
			ResultSet rs = stmt.executeQuery(); // Send the Statement to the DB
			stmt.close();
			log.info("Retrieved search results"); // Logging
			
			// Iterate through the response, one row at a time
			while(rs.next()) {
				// Set game data
				int id = rs.getInt("id");
				String name = rs.getString("name");
				GENRE genre = GENRE.valueOf(rs.getString("genre"));
				CONSOLES console = CONSOLES.valueOf(rs.getString("console"));
				String publisher = rs.getString("publisher");
				String developer = rs.getString("developer");
				int yearReleased = rs.getInt("year_released");
				boolean multiplayer = rs.getBoolean("multiplayer");
				
				// Logging
				log.info("Searched for game");
				MDC.put("searchGameId", Integer.toString(id));
				MDC.put("searchName", name);
				MDC.put("searchGenre", genre.toString());
				MDC.put("searchConsole", console.toString());
				MDC.put("searchPublisher", publisher);
				MDC.put("searchDeveloper", developer);
				MDC.put("searchYearReleased", Integer.toString(yearReleased));
				MDC.put("searchMultiplayer", Boolean.toString(multiplayer));
					
				allGames.add(new Game(id, name, genre, console, publisher, developer, yearReleased, multiplayer));
			}
			
		} catch(SQLException e) {
			log.error("We failed to retrieve searched games", e); // Logging
			return new ArrayList<>();
		}
		
		return allGames;
	}
	
	@Override
	public int insert(Game g) {
		try(Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "INSERT INTO vg_rentals.games (name, genre, console, publisher, developer, year_released, multiplayer) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING vg_rentals.games.id";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			// The positions of the '?'s begin at position 1, insert values
			stmt.setString(1, g.getName());
			stmt.setObject(2, g.getGenre(), Types.OTHER);
			stmt.setObject(3, g.getConsole(), Types.OTHER);	
			stmt.setString(4, g.getPublisher());
			stmt.setString(5, g.getDeveloper());
			stmt.setInt(6, g.getYearReleased());
			stmt.setBoolean(7, g.isMultiplayer());
			
			ResultSet rs;
			
			if( (rs = stmt.executeQuery()) != null) { // Query executed successfully
				rs.next(); // Pointer starts before first record in returned result set
				int id = rs.getInt(1); // Grab id of inserted game
				
				// Logging
				log.info("Inserted new game");
				MDC.put("insertGameId", Integer.toString(id));
				stmt.close();
				return id;
			}
			
		} catch (SQLException e) {
			log.error("We failed to insert a new game", e); // Logging
			return -1;
		}
		
		return -1;
	}

	@Override
	public boolean update(Game g) {
		try(Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "UPDATE vg_rentals.games SET name = ?, genre = ?, console = ?,"
					+ "publisher = ?, developer = ?, year_released = ?, multiplayer = ?, available = ? "
					+ "WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			// The positions of the '?'s begin at position 1, insert values
			stmt.setString(1, g.getName());
			stmt.setObject(2, g.getGenre(), Types.OTHER);
			stmt.setObject(3, g.getConsole(), Types.OTHER);	
			stmt.setString(4, g.getPublisher());
			stmt.setString(5, g.getDeveloper());
			stmt.setInt(6, g.getYearReleased());
			stmt.setBoolean(7, g.isMultiplayer());
			stmt.setBoolean(8, g.isAvailable());
			stmt.setInt(9, g.getId());
			
			if (stmt.executeUpdate() == 1) { // Query executed successfully
				// Logging
				log.info("Updated game");
				MDC.put("updateGameId", Integer.toString(g.getId()));
				stmt.close();
				return true;
			} else {
				stmt.close();
				return false;
			}
			
		} catch (SQLException e) {
			log.error("We failed to update game", e); // Logging
			return false;
		}
	}

	@Override
	public boolean delete(int gameId) {
		try(Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "DELETE FROM vg_rentals.games WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, gameId);
			
			if (stmt.executeUpdate() == 1) {  // Query executed successfully
				// Logging
				log.info("Deleted game");
				MDC.put("deleteGameId", Integer.toString(gameId));
				stmt.close();
				return true;
			} else {
				stmt.close();
				return false;
			}
			
		} catch (SQLException e) {
			log.error("We failed to delete user", e); // Logging
			return false;
		}
	}

}
