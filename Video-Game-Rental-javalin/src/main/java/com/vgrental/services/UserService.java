package com.vgrental.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.vgrental.exceptions.RegisterUserFailedException;
import com.vgrental.exceptions.UpdateUserFailedException;
import com.vgrental.models.User;
import com.vgrental.repositories.IUserDAO;
import com.vgrental.repositories.UserDAO;

public class UserService {
	
	private IUserDAO userDAO = new UserDAO();
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	public User register(User u) {
		// Logging
		MDC.put("event", "Register");
		log.info("Registering new User");
		
		// Make sure user does not already exist
		if(u.getId() != 0) {			
			throw new RegisterUserFailedException("Received User object did not have ID = 0");
		}
		
		int generatedId = userDAO.insert(u); // Insert the new User Record
		
		// Make sure new user was generated in database
		if(generatedId != -1 && generatedId != u.getId()) {			
			u.setId(generatedId);
		} else {
			throw new RegisterUserFailedException("Failed to insert the User record");
		}
		
		// Logging
		log.info("Successfully registered User");
		MDC.put("userId", Integer.toString(u.getId()));	
		
		return u;
	}
	
	public User findById(int id) {
		return userDAO.findById(id);
	}
	
	public User findByUsername(String username) {
		return userDAO.findByUsername(username);
	}
	
	public boolean update(User u) {
		// Make sure user exists
		if(u.getId() == 0) {			
			throw new UpdateUserFailedException("Received User object where ID = 0, user does not exist");
		}
		
		// Make sure user was updated successfully to database
		if(!userDAO.update(u)) {
			throw new UpdateUserFailedException("Failed to update the User record");
		}
		
		return true;
	}
	
	public boolean deleteUser(int id) {
		// Make sure user exists
		if(this.findById(id) == null) {
			// Logging
			log.error("User does not exist");
			MDC.put("userId", Integer.toString(id));
			return false;
		}
		
		return userDAO.delete(id); // Delete user
	}
	
	public List<User> findAll() {
		return userDAO.findAll();
	}
}
