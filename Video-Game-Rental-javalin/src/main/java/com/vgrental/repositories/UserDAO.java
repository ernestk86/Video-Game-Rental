package com.vgrental.repositories;

import java.sql.Connection;
import java.sql.Date;
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

import com.vgrental.models.STATES;
import com.vgrental.models.User;
import com.vgrental.util.ConnectionUtil;

public class UserDAO implements IUserDAO {
	
	private static final Logger log = LoggerFactory.getLogger(UserDAO.class);

	@Override	
	public List<User> findAll() {
		List<User> allUsers = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			Statement stmt = conn.createStatement();			
			String sql = "SELECT * FROM vg_rentals.users";	
			
			ResultSet rs = stmt.executeQuery(sql);  // Execute query on DB
			stmt.close();
			
			while(rs.next()) {
				// Set user data to return
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String dateOfBirth = rs.getString("date_of_birth");
				String address = rs.getString("address");
				String city = rs.getString("city");
				STATES state = STATES.valueOf(rs.getString("state"));
				int zipCode = rs.getInt("zip_code");
					
				// Add user to list
				allUsers.add(new User(id, username, password, dateOfBirth, address, city, state, zipCode));
			}
			
		} catch(SQLException e) {
			// Logging
			log.error("We failed to retrieve all users", e);
			return new ArrayList<>();
		}
		
		// Logging
		log.info("Retrieved all users");
		MDC.put("allUsers", "1");
		
		return allUsers;
	}

	@Override
	public User findById(int userId) {
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "SELECT * FROM vg_rentals.users WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			
			ResultSet rs = stmt.executeQuery(); // Execute query on DB
			stmt.close();
			rs.next(); // Pointer starts before first entry
			
			// Set user data to return
			int id = rs.getInt("id");
			String username = rs.getString("username");
			String password = rs.getString("password");
			String dateOfBirth = rs.getString("date_of_birth");
			String address = rs.getString("address");
			String city = rs.getString("city");
			STATES state = STATES.valueOf(rs.getString("state"));
			int zipCode = rs.getInt("zip_code");
			
			// Logging
			log.info("Found user");
			MDC.put("foundUserId", Integer.toString(id));
			
			return new User(id, username, password, dateOfBirth, address, city, state, zipCode);
			
		} catch(SQLException e) {
			log.error("We failed to retrieve user", e); // Logging
			return null;
		}
	}

	@Override
	public User findByUsername(String user_name) {
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "SELECT * FROM vg_rentals.users WHERE username = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, user_name);
			
			ResultSet rs = stmt.executeQuery(); // Execute query on DB
			stmt.close();
			rs.next(); // Pointer starts before first entry
			
			// Set user data to return
			int id = rs.getInt("id");
			String username = rs.getString("username");
			String password = rs.getString("password");
			String dateOfBirth = rs.getString("date_of_birth");
			String address = rs.getString("address");
			String city = rs.getString("city");
			STATES state = STATES.valueOf(rs.getString("state"));
			int zipCode = rs.getInt("zip_code");
			
			// Logging
			log.info("Found user");
			MDC.put("foundUsername", username);
			
			return new User(id, username, password, dateOfBirth, address, city, state, zipCode);
			
		} catch(SQLException e) {
			log.error("We failed to retrieve user", e); // Logging
			return null;
		}
	}

	@Override
	public int insert(User u) {
		try(Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "INSERT INTO vg_rentals.users (username, password, date_of_birth, address, city, state, zip_code) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING vg_rentals.users.id";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			// The positions of the '?'s begin at position 1, Inject values into '?'s
			stmt.setString(1, u.getUsername());
			stmt.setString(2, u.getPassword());	
			stmt.setDate(3, Date.valueOf(u.getDateOfBirth()));
			stmt.setString(4, u.getAddress());
			stmt.setString(5, u.getCity());
			stmt.setObject(6, u.getState(), Types.OTHER);
			stmt.setInt(7, u.getZipCode());
			
			ResultSet rs;
			
			if( (rs = stmt.executeQuery()) != null) { // Execute query on DB
				rs.next(); // Pointer starts before first entry	
				int id = rs.getInt(1);
				
				// Logging
				log.info("Inserted user");
				MDC.put("insertUserId", Integer.toString(id));
				stmt.close();
				return id;
			}
			
		} catch (SQLException e) {
			log.error("We failed to insert a new user", e); // Logging
			return -1;
		}
		
		return -1;
	}

	@Override
	public boolean update(User u) {
		try(Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "UPDATE vg_rentals.users SET username = ?, password = ?, date_of_birth = ?,"
					+ "address = ?, city = ?, state = ?, zip_code = ? WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			// The positions of the '?'s begin at position 1, Inject values into '?'s
			stmt.setString(1, u.getUsername());
			stmt.setString(2, u.getPassword());
			stmt.setDate(3, Date.valueOf(u.getDateOfBirth()));
			stmt.setString(4, u.getAddress());
			stmt.setString(5, u.getCity());
			stmt.setObject(6, u.getState(), Types.OTHER);
			stmt.setInt(7, u.getZipCode());
			stmt.setInt(8, u.getId());
			
			if (stmt.executeUpdate() == 1) { // Execute query on DB
				// Logging
				log.info("Updated user");
				MDC.put("updateUserId", Integer.toString(u.getId()));
				stmt.close();
				return true;
			} else {
				stmt.close();
				return false;
			}
			
		} catch (SQLException e) {
			log.error("We failed to insert a new user", e); // Logging
			return false;
		}
	}

	@Override
	public boolean delete(int userId) {
		try(Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "DELETE FROM vg_rentals.users WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			
			if (stmt.executeUpdate() == 1) { // Execute query on DB
				// Logging
				log.info("Deleted user");
				MDC.put("deleteUserId", Integer.toString(userId));
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
