package com.codecool.spaceship.model;

public class NoSuchPartException extends Exception {

    public NoSuchPartException() {
    }

    public NoSuchPartException(String message) {
        super(message);
    }
}
