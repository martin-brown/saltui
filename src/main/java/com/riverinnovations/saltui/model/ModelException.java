package com.riverinnovations.saltui.model;

/**
 * Root exception for all model exceptions.
 */
public class ModelException extends Exception {
    public ModelException(String s) {
        super(s);
    }

    public ModelException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
