package com.riverinnovations.saltui.model;

/**
 * Exception to be thrown when a YAML file cannot be read for some reason.
 */
public class BadYamlException extends Exception {

    public BadYamlException(String s) {
        super(s);
    }

    public BadYamlException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BadYamlException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
