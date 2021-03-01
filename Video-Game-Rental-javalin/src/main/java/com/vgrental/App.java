package com.vgrental;

import com.vgrental.controllers.Controller;
import com.vgrental.controllers.GameController;
import com.vgrental.controllers.RentalController;
import com.vgrental.controllers.ReviewController;
import com.vgrental.controllers.UserController;
import com.vgrental.exceptions.handlers.ExceptionHandler;
import com.vgrental.exceptions.handlers.GlobalExceptionHandler;

import io.javalin.Javalin;

public class App {

	private static Javalin app;

	public static void main(String[] args) {
		app = Javalin.create( config -> {
			
			config.defaultContentType = "application/json";
			
			config.enforceSsl = false;
			
			config.ignoreTrailingSlashes = true;
			
			config.enableCorsForAllOrigins();
			
			config.enableDevLogging();
		});
		
		configure(new UserController(), new GameController(), new ReviewController(), new RentalController());
		
		app.start(7000);
		
		addExceptionHandlers(new GlobalExceptionHandler());
	}

	public static void configure(Controller... controllers) {
		for(Controller c : controllers) {
			c.addRoutes(app);
		}
	}
	
	public static void addExceptionHandlers(ExceptionHandler... handlers) {
		for(ExceptionHandler handler : handlers) {
			handler.addHandlers(app);
		}
	}
}
