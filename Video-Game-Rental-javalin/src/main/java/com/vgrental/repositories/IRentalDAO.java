package com.vgrental.repositories;

import java.util.Stack;

import com.vgrental.models.Rental;
import com.vgrental.models.User;

public interface IRentalDAO {
	public Rental findRental(Rental r); // Returns full rental data
	public Stack<Object> findUserRentals(User u); // DEPRECATED REFER TO METHOD BELOW
	public Stack<Object> findUserRentalGameData(User u); // Returns full rental data
	public boolean insert(Rental r); // Returns true if successful
	public boolean update(Rental r); // Returns true if successful
	public boolean delete(Rental r); // Returns true if successful
}
