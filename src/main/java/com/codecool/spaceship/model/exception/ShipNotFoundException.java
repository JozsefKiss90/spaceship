package com.codecool.spaceship.model.exception;

public class ShipNotFoundException extends  Exception{
    public ShipNotFoundException() {
    }

    public ShipNotFoundException(String message) {
        super(message);
    }
}
