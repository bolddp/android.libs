package com.idimsoftware.www.androidcommon.web;

/**
 * Created by Daniel on 2016-10-27.
 */
public class DownloadResult<TResult> {
    // Private methods
    private int _statusCode;
    private String _errorMessage;
    private TResult _result;

    // Constructors
    public DownloadResult(int _statusCode, String _errorMessage) {
        this._statusCode = _statusCode;
        this._errorMessage = _errorMessage;
    }
    public DownloadResult(TResult _result) {
        this._result = _result;
    }

    public int getStatusCode() {
        return _statusCode;
    }

    public String getErrorMessage() {
        return _errorMessage;
    }

    public TResult getResult() {
        return _result;
    }
}
