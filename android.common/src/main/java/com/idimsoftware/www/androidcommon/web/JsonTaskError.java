package com.idimsoftware.www.androidcommon.web;

/*
 * Represents an error that has occured client- or serverside when
 * performing a JsonTask to a server.
 */
public class JsonTaskError {

	// Constructor
	
	public JsonTaskError() {
	}

	public JsonTaskError(int id, String message){
		Id = id;
		Message = message;
	}
	
	// Properties

	/*
	 * The status code that was returned together with this error.
	 */
	public int HttpStatusCode;
	
	public int Id;
	
	public String Message;
	
	public String Stacktrace;
}
