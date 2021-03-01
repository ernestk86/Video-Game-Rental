package com.vgrental.exceptions.handlers;

import io.javalin.Javalin;

public interface ExceptionHandler {

	public void addHandlers(Javalin app);
}
