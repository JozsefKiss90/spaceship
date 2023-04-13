package com.codecool.spaceship.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Base {
    private String name;
    //private User user;
    private BaseStorage storage;
    private Hangar hangar;

    public Base() {

    }

    public void setName(String name) {
        this.name = name;
    }
}
