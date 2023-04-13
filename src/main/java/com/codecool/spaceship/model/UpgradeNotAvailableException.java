package com.codecool.spaceship.model;

public class UpgradeNotAvailableException extends Exception {
    public UpgradeNotAvailableException() {
    }
    public UpgradeNotAvailableException(String message) {
        super(message);
    }
}
