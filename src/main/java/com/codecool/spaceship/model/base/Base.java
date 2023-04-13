package com.codecool.spaceship.model.base;

import java.util.UUID;

public class Base {
    private String name;
    //private User user;
    private final UUID id;
    private final BaseStorage storage;
    private final Hangar hangar;

    public Base(String name) {
        this.name = name;
        this.id=UUID.randomUUID();
        this.storage = new BaseStorage();
        this.hangar = new Hangar();
    }



}
