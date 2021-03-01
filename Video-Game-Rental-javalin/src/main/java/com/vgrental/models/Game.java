package com.vgrental.models;

import java.io.Serializable;
import java.util.Objects;

public class Game implements Serializable {

	private static final long serialVersionUID = 96218661706434129L;

	private int id;
	private String name;
	private GENRE genre;
	private CONSOLES console;
	private String publisher;
	private String developer;
	private int yearReleased;
	private boolean multiplayer;
	private boolean available;
	private double avgRating;

	public Game() {
		super();
	}

	public Game(int id, String name, GENRE genre, CONSOLES console, String publisher, String developer,
			int yearReleased) {
		super();
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.console = console;
		this.publisher = publisher;
		this.developer = developer;
		this.yearReleased = yearReleased;
		this.multiplayer = false;
		this.available = true;
		this.avgRating = 0;
	}

	public Game(int id, String name, GENRE genre, CONSOLES console, String publisher, String developer,
			int yearReleased, boolean multiplayer) {
		super();
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.console = console;
		this.publisher = publisher;
		this.developer = developer;
		this.yearReleased = yearReleased;
		this.multiplayer = multiplayer;
		this.available = true;
		this.avgRating = 0;
	}

	public Game(int id, String name, GENRE genre, CONSOLES console, String publisher, String developer,
			int yearReleased, boolean multiplayer, boolean available) {
		super();
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.console = console;
		this.publisher = publisher;
		this.developer = developer;
		this.yearReleased = yearReleased;
		this.multiplayer = multiplayer;
		this.available = available;
		this.avgRating = 0;
	}

	public Game(int id, String name, GENRE genre, CONSOLES console, String publisher, String developer,
			int yearReleased, boolean multiplayer, boolean available, double avgRating) {
		super();
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.console = console;
		this.publisher = publisher;
		this.developer = developer;
		this.yearReleased = yearReleased;
		this.multiplayer = multiplayer;
		this.available = available;
		this.avgRating = avgRating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GENRE getGenre() {
		return genre;
	}

	public void setGenre(GENRE genre) {
		this.genre = genre;
	}

	public CONSOLES getConsole() {
		return console;
	}

	public void setConsole(CONSOLES console) {
		this.console = console;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public int getYearReleased() {
		return yearReleased;
	}

	public void setYearReleased(int yearReleased) {
		this.yearReleased = yearReleased;
	}

	public boolean isMultiplayer() {
		return multiplayer;
	}

	public void setMultiplayer(boolean multiplayer) {
		this.multiplayer = multiplayer;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public int hashCode() {
		return Objects.hash(available, console, developer, genre, id, multiplayer, name, publisher, yearReleased);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		return available == other.available && console == other.console && Objects.equals(developer, other.developer)
				&& genre == other.genre && id == other.id && multiplayer == other.multiplayer
				&& Objects.equals(name, other.name) && Objects.equals(publisher, other.publisher)
				&& yearReleased == other.yearReleased;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", name=" + name + ", genre=" + genre + ", console=" + console + ", publisher="
				+ publisher + ", developer=" + developer + ", yearReleased=" + yearReleased + ", multiplayer="
				+ multiplayer + ", available=" + available + "]";
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}
}
