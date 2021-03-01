package com.vgrental.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.vgrental.models.CONSOLES;
import com.vgrental.models.Rental;
import com.vgrental.models.User;
import com.vgrental.util.ConnectionUtil;

public class RentalDAO implements IRentalDAO {
	
	private static final Logger log = LoggerFactory.getLogger(RentalDAO.class);

	@Override
	public Rental findRental(Rental r) {
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "SELECT * FROM vg_rentals.rentals WHERE user_id = ? AND game_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, r.getUserId());
			stmt.setInt(2, r.getGameId());
			
			ResultSet rs = stmt.executeQuery();	// Execute query on DB
			stmt.close();
			rs.next(); // Pointer begins before first entry
			
			// Set rental data to return
			int userId = rs.getInt("user_id");
			int gameId = rs.getInt("game_id");
			String dueDate = rs.getString("due_date");
			boolean overDue = rs.getBoolean("overdue");
			
			// Logging
			log.info("Found rental");
			MDC.put("foundUserId", Integer.toString(userId));
			MDC.put("foundGameId", Integer.toString(gameId));
			
			return new Rental(userId, gameId, dueDate, overDue);
			
		} catch(SQLException e) {
			log.error("We failed to retrieve all reviews for the game", e); // Logging
			return null;
		}
	}
	
	@Override
	public Stack<Object> findUserRentals(User u) {
		Stack<Object> allUserRentals = new Stack<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "SELECT * FROM vg_rentals.rentals WHERE user_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, u.getId());
			
			ResultSet rs = stmt.executeQuery(); // Execute query on DB
			stmt.close();
			
			while(rs.next()) {
				// Set rental data to return
				int userId = rs.getInt("user_id");
				int gameId = rs.getInt("game_id");
				String dueDate = rs.getString("due_date");
				boolean overDue = rs.getBoolean("overdue");
				
				// Add rental to list
				allUserRentals.push(new Rental(userId, gameId, dueDate, overDue));
			}
			
		} catch(SQLException e) {
			log.error("We failed to retrieve all reviews for the game", e); // Logging
			return new Stack<>();
		}
		
		// Logging
		log.info("Found user rentals");
		MDC.put("foundRentalsUserId", Integer.toString(u.getId()));
		
		return allUserRentals;
	}
	
	public Stack<Object> findUserRentalGameData(User u) {
		Stack<Object> allUserRentalGameData = new Stack<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "SELECT name, console, publisher, due_date, overdue, game_id "
					+ "FROM vg_rentals.rentals INNER JOIN vg_rentals.games ON rentals.game_id = games.id "
					+ "WHERE rentals.user_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, u.getId());
			
			ResultSet rs = stmt.executeQuery(); // Query executed successfully
			stmt.close();
			
			while(rs.next()) {
				// Set rental data for return
				int gameId = rs.getInt("game_id");
				String name = rs.getString("name");
				CONSOLES console = CONSOLES.valueOf(rs.getString("console"));
				String publisher = rs.getString("publisher");
				String dueDate = rs.getString("due_date");
				boolean overDue = rs.getBoolean("overdue");				
				
				// Add rental to list to return
				allUserRentalGameData.push(new Rental(u.getId(), gameId, dueDate, overDue, name, console, publisher));
			}
			
		} catch(SQLException e) {
			log.error("We failed to retrieve all reviews for the game", e); // Logging
			return new Stack<>();
		}

		// Logging
		log.info("Found user rentals");
		MDC.put("foundRentalsUserId", Integer.toString(u.getId()));
		
		return allUserRentalGameData;
	}

	@Override
	public boolean insert(Rental r) {
		
		try(Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "INSERT INTO vg_rentals.rentals (user_id, game_id) "
					+ "VALUES (?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, r.getUserId());
			stmt.setInt(2, r.getGameId());
			
			if (stmt.executeUpdate() == 1) { // Query executed successfully
				// Logging
				log.info("Inserted rental");
				MDC.put("insertUserId", Integer.toString(r.getUserId()));
				MDC.put("insertGameId", Integer.toString(r.getGameId()));
				stmt.close();
				return true;
			} else {
				stmt.close();
				return false;
			}
			
		} catch (SQLException e) {
			log.error("We failed to insert a new rental", e); // Logging
			return false;
		}
	}

	@Override
	public boolean update(Rental r) {
		
		try(Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "UPDATE vg_rentals.rentals SET overdue = ?, due_date = ?"
					+ "WHERE user_id = ? AND game_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setBoolean(1, r.isOverDue());
			stmt.setDate(2, Date.valueOf(r.getDueDate()));
			stmt.setInt(3, r.getUserId());
			stmt.setInt(4,  r.getGameId());
			
			if (stmt.executeUpdate() == 1) { // Query executed successfully
				// Logging
				log.info("Updated rental");
				MDC.put("updateUserId", Integer.toString(r.getUserId()));
				MDC.put("updateGameId", Integer.toString(r.getGameId()));
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
	public boolean delete(Rental r) {
		try(Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "DELETE FROM vg_rentals.rentals WHERE user_id = ? AND game_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, r.getUserId());
			stmt.setInt(2, r.getGameId());
			
			if (stmt.executeUpdate() == 1) { // Query executed successfully
				// Logging
				log.info("Deleted rental");
				MDC.put("deleteUserId", Integer.toString(r.getUserId()));
				MDC.put("deleteGameId", Integer.toString(r.getGameId()));
				stmt.close();
				return true;
			} else {
				stmt.close();
				return false;
			}
			
		} catch (SQLException e) {
			log.error("We failed to delete rental", e); // Logging
			return false;
		}
	}

}
