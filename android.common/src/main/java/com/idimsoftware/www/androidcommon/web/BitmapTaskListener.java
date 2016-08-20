package com.idimsoftware.www.androidcommon.web;

import android.graphics.Bitmap;

/**
 * Created by Daniel on 2016-08-19.
 */
public interface BitmapTaskListener {
    void onBitmapDownloaded(Bitmap bitmap);

    void onError(int statusCode, String message);
}
