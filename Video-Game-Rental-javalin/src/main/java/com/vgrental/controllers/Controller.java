package com.vgrental.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

public interface Controller {
	
	// Checks if user has successfully logged into the system
	// Returns true if user has successfully logged in
	// Checks session if username key is true
	static boolean authenticate (Context ctx, String username) {
		if(ctx.sessionAttribute(username) != null) {
			return ctx.sessionAttribute(username);
		} else {
			return false;
		}
	}

	public void addRoutes(Javalin app);
	/*
	 * 		USER DATA
	 * 		int id; // MUST BE 0 WHEN REGISTERING NEW USER
	 *		String username; // MUST BEGIN WITH A LETTER
	 *		String password;
	 *		String dateOfBirth; // FORMAT MUST BE YYYY-MM-DD
	 *		String address;
	 *		String city;
	 *		STATES state; // MUST BE ENUM OF STATES (STATE INITIALS)
	 *		int zipCode; // MUST BE BETWEEN 10000-99999 INCLUSIVE
	 * 
	 * 		GET /users
	 * 			Returns all user data
	 * 		GET /users/:id
	 * 			Returns user data along with rentals MUST BE AUTHENTICATED
	 * 		POST /users/authenticate
	 * 			Redirects to the user's profile page if successful
	 * 			JSON must supply username and password
	 * 		POST /users
	 * 			Creates new user
	 * 			JSON must supply all fields, id must be 0
	 * 		GET /users/logout/:username
	 * 			Logs out of user account
	 * 		PUT /users
	 * 			Updates user MUST BE AUTHENTICATED
	 * 			JSON with keys: id, username, password, dateOfBirth, address, city, state, zipCode
	 * 		DELETE /users/:id
	 * 			Deletes user MUST BE AUTHENTICATED ALSO USER MUST HAVE NO RENTALS OUT
	 * 
	 * 		REVIEW DATA
	 * 		int id; // MUST BE 0 WHEN REGISTERING NEW REVIEW
	 *		int gameId;
	 *		int rating; // MUST BE BETWEEN 1-10 INCLUSIVE
	 *		String writtenReview;
	 *
	 *		GET /reviews
	 *			Returns all review data for every review for every game
	 *		POST /reviews/:username
	 *			Creates new review, MUST BE AUTHENTICATED
	 *			JSON must supply all fields, id must be 0
	 *		DELETE /reviews/:id
	 *			Deletes review MUST BE AUTHENTICATED AS ADMIN
	 *
	 *		RENTAL DATA
	 *		int userId;
	 *		int gameId;
	 *		String dueDate; // NOT REQUIRED DEFAULTS TO 14 DAYS FROM TODAY, CANNOT BE MORE THAN 14 DAYS FROM TODAY
	 *		boolean overDue; // NOT REQUIRED DEFAULTS TO FALSE
	 *		String name; // NOT REQUIRED DEFAULTS TO NOT SURE
	 *		CONSOLES console; // NOT REQUIRED DEFAULTS TO SNES, LOOK FOR CONSOLES ENUM
	 *		String publisher; // NOT REQUIRED DEFUALTS TO NOT SURE
	 *
	 *		GET /rent/:username
	 *			Retrieves the rentals of the user with the given username
	 *		POST /rent
	 *			Creates a new rental MUST BE AUTHENTICATED
	 *			JSON must supply userId and gameId
	 *		PUT /rent/overdue
	 *			Toggles a rental as overdue or not MUST BE AUTHENTICATED AS ADMIN
	 *			JSON must supply userId and gameId
	 *		PUT /rent/changeDate
	 *			Changes the due date. Keep in mind the due date CANNOT be further than 14 days away MUST BE AUTHENTICATED AS ADMIN
	 *			JSON requires userId, gameId, and dueDate
	 *			Date format must be YYYY-MM-DD
	 *		DELETE /rent
	 *			Returns game and deletes rental MUST BE AUTHENTICATED
	 *			JSON must supply userId and gameId
	 *
	 *		GAME DATA
	 *		int id; // MUST BE 0 WHEN CREATING A NEW ENTRY
	 *		String name;
	 *		GENRE genre; // MUST BE GENRE, REFER TO ENUMS BELOW
	 *		CONSOLES console; // MUST BE CONSOLE, REFER TO ENUMS BELOW
	 *		String publisher;
	 *		String developer;
	 *		int yearReleased;
	 *		boolean multiplayer;
	 *		boolean available; // ENTER ANY FIELD DEFAULTS TO TRUE
	 *		double avgRating; // ENTER ANY FIELD DEFAULTS TO 0
	 *
	 *		GET /games
	 *			Gets a full list of all data for every game
	 *		GET /games/search/:name
	 *			Gets a list of results of games from a search term MUST BE AUTHENTICATED
	 *			Also requires JSON with username
	 *		GET /games/:id
	 *			Gets a game's data and reviews as well as average score from an id MUST BE AUTHENTICATED
	 *			Also requires JSON with username
	 *		POST /games
	 *			Creates a new game entry MUST BE AUTHENTICATED AS ADMIN
	 *			JSON must supply all fields, id must be 0
	 *		PUT /games
	 *			Updates an existing game's data MUST BE AUTHENTICATED AS ADMIN
	 *			JSON must supply all fields, id MUST MATCH GAME'S ID
	 *		DELETE /games/:id
	 *			Deletes a game MUST BE AUTHENTICATED AS ADMIN
	 *
	 *		public enum GENRE {
	 * 			ACTION_ADVENTURE,
	 * 			ROLE_PLAYING,
	 * 			STRATEGY,
	 * 			FIRST_PERSON_SHOOTER,
	 * 			VISUAL_NOVEL,
	 * 			SPORTS,
	 * 			SIMULATION,
	 * 			PUZZLE,
	 * 			PARTY,
	 * 			HORROR,
	 * 			MOBA,
	 * 			RHYTHM,
	 * 			BATTLE_ROYALE,
	 * 			FIGHTING,
	 *		}
	 *
	 *		public enum CONSOLES {
	 *			NES,
	 *			SNES,
	 *			N64,
	 *			GAMECUBE,
	 *			WII,
	 *			WIIU,
	 *			SWITCH,
	 *			GAMEBOY,
	 *			GBA,
	 *			NDS,
	 *			N3DS,
	 *			MASTERSYSTEM,
	 *			GENESIS,
	 *			SEGACD,
	 *			SATURN,
	 *			DREAMCAST,
	 *			GAMEGEAR,
	 *			ATARI2600,
	 *			JAGUAR,
	 *			PSX,
	 *			PS2,
	 *			PS3,
	 *			PS4,
	 *			PS5,
	 *			PSP,
	 *			PSVita,
	 *			XBOX,
	 *			XBOX360,
	 *			XBOXONE,
	 *			XBOXSERIES,
	 * 			PC,
	 *		}
	 */
}
