package com.vgrental.models;

import java.io.Serializable;
import java.util.Objects;

public class Rental implements Serializable{

	private static final long serialVersionUID = -5737227035901465147L;
	
	// OPTIMIZATION 
	private static final String NOTSET = "NOT SET";

	private int userId;
	private int gameId;
	private String dueDate;
	private boolean overDue;
	private String name;
	private CONSOLES console;
	private String publisher;
	
	public Rental() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Rental(int userId, int gameId) {
		super();
		this.userId = userId;
		this.gameId = gameId;
		this.dueDate = NOTSET;
		this.overDue = false;
		this.name = NOTSET;
		this.console = CONSOLES.SNES;
		this.publisher = NOTSET;
	}
	
	public Rental(int userId, int gameId, String dueDate, boolean overDue) {
		super();
		this.userId = userId;
		this.gameId = gameId;
		this.dueDate = dueDate;
		this.overDue = overDue;
		this.name = NOTSET;
		this.console = CONSOLES.SNES;
		this.publisher = NOTSET;
	}
	
	public Rental(int userId, int gameId, String dueDate, boolean overDue, String name, CONSOLES console, String publisher) {
		super();
		this.userId = userId;
		this.gameId = gameId;
		this.dueDate = dueDate;
		this.overDue = overDue;
		this.name = name;
		this.console = console;
		this.publisher = publisher;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isOverDue() {
		return overDue;
	}

	public void setOverDue(boolean overDue) {
		this.overDue = overDue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dueDate, gameId, overDue, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rental other = (Rental) obj;
		return Objects.equals(dueDate, other.dueDate) && gameId == other.gameId && overDue == other.overDue
				&& userId == other.userId;
	}

	@Override
	public String toString() {
		return "Rental [userId=" + userId + ", gameId=" + gameId + ", dueDate=" + dueDate + ", overDue=" + overDue
				+ "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
