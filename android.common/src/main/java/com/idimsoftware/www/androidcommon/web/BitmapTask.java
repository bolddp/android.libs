package com.idimsoftware.www.androidcommon.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Daniel on 2016-08-19.
 */
public class BitmapTask extends AsyncTask<String, Integer, Bitmap> {

    // Private variables

    private static final int STD_TIMEOUT = 10000;
    private static final String USER_AGENT = "BitmapTask/1.0";
    private static final String ACCEPT = "image/*";

    private BitmapTaskListener _listener;
    private int statusCode;
    private String errorMessage;


    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection httpCon = null;
        Bitmap bitmap = null;
        InputStream is = null;

        try {
            URL url = new URL(params[0]);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(STD_TIMEOUT);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Accept", ACCEPT);

            statusCode = httpCon.getResponseCode();

            is = httpCon.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

            return bitmap;
        }
        catch (Throwable e) {
            errorMessage = e.getMessage();
            return  null;
        }
            finally {
            try {
                if (httpCon != null)
                    httpCon.disconnect();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap == null)
            _listener.onError(statusCode, errorMessage);
        else
            _listener.onBitmapDownloaded(bitmap);
    }

    public BitmapTask(BitmapTaskListener listener) {
        this._listener = listener;
    }
}
