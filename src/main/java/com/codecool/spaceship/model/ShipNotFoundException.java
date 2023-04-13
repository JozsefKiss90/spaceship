package com.codecool.spaceship.model;

public class ShipNotFoundException extends  Exception{
    public ShipNotFoundException() {
    }

    public ShipNotFoundException(String message) {
        super(message);
    }
}
