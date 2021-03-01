package com.vgrental.models;

import java.io.Serializable;
import java.util.Objects;

public class Review implements Serializable {

	private static final long serialVersionUID = 3220503720048853686L;

	private int id;
	private int gameId;
	private int rating;
	private String writtenReview;
	
	public Review() {
		super();
	}

	public Review(int id, int gameId, int rating, String writtenReview) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.rating = rating;
		this.writtenReview = writtenReview;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getWrittenReview() {
		return writtenReview;
	}

	public void setWrittenReview(String writtenReview) {
		this.writtenReview = writtenReview;
	}

	@Override
	public int hashCode() {
		return Objects.hash(gameId, id, rating, writtenReview);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		return gameId == other.gameId && id == other.id && rating == other.rating
				&& Objects.equals(writtenReview, other.writtenReview);
	}

	@Override
	public String toString() {
		return "Review [id=" + id + ", gameId=" + gameId + ", rating=" + rating + ", writtenReview=" + writtenReview
				+ "]";
	}
}
