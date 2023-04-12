package com.codecool.spaceship.model;

public class UpgradeNotAvailable extends Exception {
    public UpgradeNotAvailable() {
    }
    public UpgradeNotAvailable(String message) {
        super(message);
    }
}
