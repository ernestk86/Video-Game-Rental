package com.vgrental.services;

import java.util.Stack;

import com.vgrental.models.Rental;
import com.vgrental.models.User;

public interface IRentalService {
	public Rental findRental(int userId, int gameId); // Returns rental from user and game ids
	public Stack<Object> findUserRentals(User u); // DEPRECATED, LOOK TO METHOD BELOW
	public Stack<Object> findUserRentalGameData(User u); // Returns full rental data for every rental that user has
	public boolean rentGame(Rental r); // Returns true if game is available for rent. Sets game as unavailable for rent if rent is successful
	public boolean returnGame(Rental r); // Returns true if game is unavailable for rent. Sets game as available for rent if return is successful
	public boolean changeDueDate(Rental r, String newDueDate); // Returns true if rental's due date is changed. 
	// newDueDate must be in following format: "YYYY-MM-DD"
	// newDueDate CONSTRAINT: date cannot be over 14 days from present day
	public void sendEmail(Rental r); // Sends email to let user know that game is overdue
	public boolean toggleOverdue(Rental r); // Toggles a rental overdue or not. Sends email automatically to user of rental
}
