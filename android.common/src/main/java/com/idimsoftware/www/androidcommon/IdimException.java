package com.idimsoftware.www.androidcommon;

/**
 * Base exception class with a message and an error code that
 * is convenient for instance to display localized error messages.
 */
public class IdimException extends Exception {

    // Private variables

    private int _errorId;

    // Constructor

    public IdimException(int errorId, String detailMessage) {
        super(detailMessage);
        _errorId = errorId;
    }

    // Properties

    public int getErrorId(){
        return _errorId;
    }
}
