package com.vgrental.services;

import java.util.List;
import java.util.Stack;

import com.vgrental.models.Review;

public interface IReviewService {
	public List<Review> findAll(); // Returns a list of every review for every game
	public Stack<Object> getReviews(int id); // Returns a stack of every review for a single game, insert gameId
	public Review findReview(int id); // Returns a specific review for a specific game
	public Review addReview(Review r); // Returns review object with all data including review id, insert review with id = 0
	public boolean deleteReview(Review r); // Returns true if review is successfully deleted
}
