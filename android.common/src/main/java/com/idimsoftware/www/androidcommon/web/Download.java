package com.idimsoftware.www.androidcommon.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.idimsoftware.www.androidcommon.FlushedInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Provides functionality for downloading content from the web.
 */
public class Download {

    // Constants

    public static final String HTTP_GET = "GET";
    public static final String HTTP_PUT = "PUT";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_DELETE = "DELETE";

    public static final String ENCODING_JSON = "UTF8";
    public static final int TIMEOUT_JSON = 10000;
    public static final String ACCEPT_JSON = "application/json";

    public static final int TIMEOUT_BITMAP = 20000;
    public static final String ACCEPT_BITMAP = "image/*";

    // Private variables

    private static String _userAgent = "IdimDownload/1.0";

    // Methods

    public static DownloadResult<String> json(String urlString) {
        HttpURLConnection httpCon = null;
        InputStream inStream = null;
        try {
            URL url = new URL(urlString);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(TIMEOUT_JSON);
            httpCon.setRequestProperty("User-Agent", _userAgent);
            httpCon.setRequestProperty("Accept", ACCEPT_JSON);
            httpCon.setDoInput(true);
            httpCon.setDoOutput(false);
            httpCon.setRequestMethod(HTTP_GET);

            int responseCode = httpCon.getResponseCode();

            // Get the response or the error stream
            if (responseCode < 300)
                inStream = new FlushedInputStream(httpCon.getInputStream());
            else
                inStream = new FlushedInputStream(httpCon.getErrorStream());

            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(inStream, ENCODING_JSON));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line + "\n");
                }

                return new DownloadResult<String>(sb.toString());
            } finally {
                inStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            // If we receive an error here, it means that there was no response
            return new DownloadResult(HttpURLConnection.HTTP_NOT_FOUND, ex.getMessage());
        }
        finally {
            try {
                if (httpCon != null)
                    httpCon.disconnect();
                if (inStream != null)
                    inStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    Downloads a bitmap from a URL.
     */
    public static DownloadResult<Bitmap> bitmap(String urlString) {
        HttpURLConnection httpCon = null;
        InputStream inStream = null;
        try {
            URL url = new URL(urlString);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(TIMEOUT_BITMAP);
            httpCon.setRequestProperty("User-Agent", _userAgent);
            httpCon.setRequestProperty("Accept", ACCEPT_BITMAP);

            int statusCode = httpCon.getResponseCode();

            inStream = httpCon.getInputStream();
            return new DownloadResult(BitmapFactory.decodeStream(inStream));
        } catch (IOException ex) {
            ex.printStackTrace();
            // If we receive an error here, it means that there was no response
            return new DownloadResult(HttpURLConnection.HTTP_NOT_FOUND, ex.getMessage());
        }
    }

    public static void setUserAgent(String userAgent) {
        _userAgent = userAgent;
    }
}
