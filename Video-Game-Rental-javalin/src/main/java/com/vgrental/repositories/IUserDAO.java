package com.vgrental.repositories;

import java.util.List;

import com.vgrental.models.User;

public interface IUserDAO {

	public List<User> findAll(); // Returns every user
	public User findById(int userId); // Returns user
	public User findByUsername(String username); // Same as above
	public int insert(User u); // Returns the generated primary key
	public boolean update(User u); // Returns true if operation is successful
	public boolean delete(int userId); // Returns true if operation is successful
}
