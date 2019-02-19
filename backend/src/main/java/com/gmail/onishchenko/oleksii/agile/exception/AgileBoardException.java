package com.gmail.onishchenko.oleksii.agile.exception;

public class AgileBoardException extends RuntimeException {

    private static final long serialVersionUID = -5660291403570702131L;

    public AgileBoardException() {
    }

    public AgileBoardException(String message) {
        super(message);
    }
}
