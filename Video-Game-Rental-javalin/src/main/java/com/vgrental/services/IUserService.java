package com.vgrental.services;

import java.util.List;

import com.vgrental.models.User;

public interface IUserService {
	public User register(User u); // Returns user with all user data including id. Insert user data with id = 0
	public User findById(int id); // Returns user from id
	public User findByUsername(String username); // Returns user from username
	public boolean update(User u); // Returns true if successful
	public boolean deleteUser(int id); // Returns true if successful
	public List<User> findAll(); // Returns List of all users with full data
}
