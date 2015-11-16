package com.idimsoftware.www.androidcommon.web;

public class JsonTaskResult {
	
	// Private variables
	
	private int _statusCode;
	private String _jsonData;
	
	// Constructor
	
	public JsonTaskResult(int statusCode, String jsonData) {
		_statusCode = statusCode;
		_jsonData = jsonData;
	}
	
	public int getStatusCode() {
		return _statusCode;
	}
	
	public String getJsonData() {
		return _jsonData;
	}
}
