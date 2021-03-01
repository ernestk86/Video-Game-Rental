package com.vgrental.repositories;

import java.util.List;
import java.util.Stack;

import com.vgrental.models.Review;

public interface IReviewDAO {
	public List<Review> findAll();  // Returns list of every review for every game
	public Stack<Object> findByGameId(int gameId); // Returns a stack of every review for a single game
	public Review findByReviewId(int reviewId); // Returns a review
	public int insert(Review r); // Returns the generated primary key
	public boolean delete(Review r); // Returns true if successful
	public double getGameAvg(int gameId); // Returns average rating of game
}
