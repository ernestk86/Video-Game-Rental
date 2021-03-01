package com.vgrental.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.vgrental.models.CONSOLES;
import com.vgrental.models.GENRE;
import com.vgrental.models.Game;
import com.vgrental.models.Review;
import com.vgrental.util.ConnectionUtil;

public class ReviewDAO implements IReviewDAO {
	
	private static final Logger log = LoggerFactory.getLogger(ReviewDAO.class);

	@Override
	public List<Review> findAll() {
		List<Review> allReviews = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query			
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM vg_rentals.reviews";
			
			ResultSet rs = stmt.executeQuery(sql);  // Execute query on DB
			stmt.close();
			
			// Pointer starts before first entry in result set
			while(rs.next()) {
				// Set review data for return				
				int id = rs.getInt("id");
				int gameId = rs.getInt("game_id");
				int rating = rs.getInt("rating");
				String writtenReview = rs.getString("review");
				
				// Add review to list for return
				allReviews.add(new Review(id, gameId, rating, writtenReview));
			}
			
		} catch(SQLException e) {
			log.error("We failed to retrieve all reviews", e); // Logging
			return new ArrayList<>();
		}
		
		// Logging
		log.info("Retrieved all reviews");
		MDC.put("allReviews", "1");
		
		return allReviews;
	}

	@Override
	public Stack<Object> findByGameId(int gameId) {
		Stack<Object> allGameReviews = new Stack<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "SELECT * FROM vg_rentals.reviews WHERE game_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, gameId);
			
			ResultSet rs = stmt.executeQuery();  // Execute query on DB
			stmt.close();
			
			// Pointer starts before first entry in result set
			while(rs.next()) {
				// Set data for list to return				
				int id = rs.getInt("id");
				int game_Id = rs.getInt("game_id");
				int rating = rs.getInt("rating");
				String writtenReview = rs.getString("review");
				
				// Add review to list
				allGameReviews.push(new Review(id, game_Id, rating, writtenReview));
			}
			
		} catch(SQLException e) {
			log.error("We failed to retrieve all reviews for the game", e); // Logging
			return new Stack<>();
		}
		
		// Logging
		log.info("Retrieved all reviews for game");
		MDC.put("reviewsGameId", Integer.toString(gameId));
		
		return allGameReviews;
	}

	public Review findByReviewId(int reviewId) {
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "SELECT * FROM vg_rentals.reviews WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);			
			stmt.setInt(1, reviewId);
			
			ResultSet rs = stmt.executeQuery(); // Execute query on DB
			stmt.close();
			
			rs.next(); // Pointer starts before first entry in result set
			
			// Set review data for return
			int id = rs.getInt("id");
			int gameId = rs.getInt("game_id");
			int rating = rs.getInt("rating");
			String writtenReview = rs.getString("review");			
			
			// Logging
			log.info("Retrieved review");
			MDC.put("foundReviewId", Integer.toString(id));
			
			return new Review(id, gameId, rating, writtenReview);
			
		} catch(SQLException e) {
			log.error("We failed to retrieve review", e); // Logging
			return null;
		}
	}
	
	@Override
	public int insert(Review r) {
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "INSERT INTO vg_rentals.reviews (game_id, rating, review) VALUES (?, ?, ?) RETURNING vg_rentals.reviews.id";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, r.getGameId());
			stmt.setInt(2, r.getRating());
			stmt.setString(3, r.getWrittenReview());
			
			ResultSet rs;
			
			if( (rs = stmt.executeQuery()) != null) { // Execute query on DB
				rs.next(); // Pointer starts before first entry in result set				
				int id = rs.getInt(1);
				
				// Logging
				log.info("Inserted review");
				MDC.put("insertReviewId", Integer.toString(id));
				stmt.close();
				return id;
			}
		} catch(SQLException e) {
			log.error("We failed to retrieve all reviews for the game", e); // Logging
			return -1;
		}
		return -1;
	}

	@Override
	public boolean delete(Review r) {
		try(Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "DELETE FROM vg_rentals.reviews WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, r.getId());
			
			stmt.executeUpdate(); // Execute query on DB
			
			// Logging
			log.info("Deleted review");
			MDC.put("deleteReviewId", Integer.toString(r.getId()));
			stmt.close();
			return true;
			
		} catch (SQLException e) {
			log.error("We failed to delete review", e); // Logging
			return false;
		}
	}

	public double getGameAvg(int gameId) {
		try (Connection conn = ConnectionUtil.getConnection()) { // Connect to DB
			// Prepare SQL query
			String sql = "SELECT AVG(rating) FROM vg_rentals.reviews WHERE game_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, gameId);
			
			ResultSet rs = stmt.executeQuery(); // Execute query on DB
			
			rs.next(); // Pointer starts before first entry in result set
			double result = rs.getDouble("avg"); // Set data for return
			
			// Logging
			log.info("Retrieved Rating Average");
			MDC.put("avg", Double.toString(result));
			MDC.put("avgGameId", Integer.toString(gameId));
			stmt.close();
			return result;
			
		} catch(SQLException e) {
			log.error("We failed to retrieve the average rating for the game", e); // Logging
			return 0.0;
		}
	}
	
}
