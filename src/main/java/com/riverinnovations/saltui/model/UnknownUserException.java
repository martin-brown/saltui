package com.riverinnovations.saltui.model;

/**
 * Thrown when an operation cannot find a matching user.
 */
public class UnknownUserException extends ModelException {
    public UnknownUserException(String s) {
        super(s);
    }

    public UnknownUserException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
