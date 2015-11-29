package com.idimsoftware.www.androidcommon.web;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.idimsoftware.www.androidcommon.FlushedInputStream;

import android.os.AsyncTask;

public class JsonTask extends AsyncTask<String, Integer, String> {

	public static final String HTTP_GET = "GET";
	public static final String HTTP_PUT = "PUT";
	public static final String HTTP_POST = "POST";
	public static final String HTTP_DELETE = "DELETE";

	// Private variables

	private static final int STD_TIMEOUT = 5000;
	private static final String ENCODING = "UTF8";
	private static final String USER_AGENT = "JsonTask/1.0";
	private static final String CONTENT_TYPE = "application/json";
	private static final String ACCEPT = "application/json";

	private String _httpVerb;
	private JsonTaskListener _listener;
	private Object _postData;
	private int _responseCode;

	// Private methods
	
	/*
	 * Creates a WebServiceError object and returns it as a JSON string which is returned
	 * to the IJsonTaskListener.
	 */
	private String SerializeWebError(int responseCode, int errorCode, String errorMessage) {
		JsonTaskError error = new JsonTaskError();
		error.HttpStatusCode = responseCode;
		error.Id = errorCode;
		error.Message = errorMessage;
		
		Gson gson = new Gson();
		return gson.toJson(error); 
	}

	// Protected methods

	@Override
	protected String doInBackground(String... params) {
		try {
			this.publishProgress(0);

			URL url = new URL(params[0]);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setConnectTimeout(STD_TIMEOUT);
			httpCon.setRequestProperty("User-Agent", USER_AGENT);
			httpCon.setRequestProperty("Content-Type", CONTENT_TYPE);
			httpCon.setRequestProperty("Accept", ACCEPT);
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setRequestMethod(_httpVerb);

			// Do we PUT or POST so we have to send the payload?
			if (_httpVerb.equalsIgnoreCase(HTTP_POST) || _httpVerb.equalsIgnoreCase(HTTP_PUT)) {
				// HTTP PUT OR POST

				// Turn the payload into a byte array
				Gson json = new Gson();
				String jsonString = json.toJson(_postData);
				byte[] bytes = jsonString.getBytes(ENCODING);

				BufferedOutputStream outStream = null;
				try {
					outStream = new BufferedOutputStream(httpCon.getOutputStream());
					outStream.write(bytes);
					outStream.flush();
				} finally {
					if (outStream != null)
						outStream.close();
				}
			}
			else{
				httpCon.setDoOutput(false);
			}

			_responseCode = httpCon.getResponseCode();

			// Get the response or the error stream
			FlushedInputStream inStream = null;
			if (_responseCode < 300)
				inStream = new FlushedInputStream(httpCon.getInputStream());
			else
				inStream = new FlushedInputStream(httpCon.getErrorStream());
			
			try {
				BufferedReader r = new BufferedReader(new InputStreamReader(inStream, ENCODING));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					sb.append(line + "\n");
				}

				this.publishProgress(100);

				return sb.toString();
			} finally {
				inStream.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
			
			// If we receive an error here, it means that there was no response
			_responseCode = HttpURLConnection.HTTP_NOT_FOUND;
			// Create an error object and return it
			return SerializeWebError(_responseCode, -1, e.getMessage());
		}
	}

	@Override
	protected void onPostExecute(String result) {
		JsonTaskResult jsonResult = new JsonTaskResult(_responseCode, result);

		if (_listener != null)
			_listener.onJsonTaskCompleted(jsonResult);
	}

	// Constructor

	/*
	 * Sets up the JsonTask to perform a HTTP GET.
	 */
	public JsonTask(JsonTaskListener listener) {
		_httpVerb = HTTP_GET;
		_listener = listener;
	}

	public JsonTask(JsonTaskListener listener, String httpVerb, Object postData) {
		_httpVerb = httpVerb;
		_listener = listener;
		_postData = postData;
	}
}
