package com.vgrental.services;

import java.util.List;
import java.util.Stack;

import com.vgrental.exceptions.RegisterUserFailedException;
import com.vgrental.models.Review;
import com.vgrental.repositories.IReviewDAO;
import com.vgrental.repositories.ReviewDAO;

public class ReviewService implements IReviewService {
	private IReviewDAO reviewDAO = new ReviewDAO();
	
	public List<Review> findAll() {
		return reviewDAO.findAll();
	}
	
	public Stack<Object> getReviews(int id) {
		return reviewDAO.findByGameId(id);
	}
	
	public Review findReview(int id) {
		return reviewDAO.findByReviewId(id);
	}
	
	public Review addReview(Review r) {
		// Make sure review doesn't already exist
		if(r.getId() != 0) {			
			throw new RegisterUserFailedException("Received Game object did not have ID = 0");
		}
		
		int generatedId = reviewDAO.insert(r); // Insert review and get review id
		
		// Make sure review was added to database successfully
		if(generatedId != -1 && generatedId != r.getId()) {
			r.setId(generatedId);
		} else {
			throw new RegisterUserFailedException("Failed to insert the User record");
		}
		
		return r;
	}
	
	public boolean deleteReview(Review r) {
		if(this.findReview(r.getId()) == null) return false; // Make sure review exists
		return reviewDAO.delete(r); // Delete review
	}
}
