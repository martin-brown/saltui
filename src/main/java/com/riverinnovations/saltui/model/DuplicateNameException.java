package com.riverinnovations.saltui.model;

/**
 * Exception thrown when two users have the same name.
 */
public class DuplicateNameException extends ModelException {
    public DuplicateNameException(String s) {
        super(s);
    }

    public DuplicateNameException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
