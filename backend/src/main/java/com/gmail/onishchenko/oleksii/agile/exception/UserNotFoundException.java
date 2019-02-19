package com.gmail.onishchenko.oleksii.agile.exception;

public class UserNotFoundException extends AgileBoardException {

    private static final long serialVersionUID = 5409852450747206766L;

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
