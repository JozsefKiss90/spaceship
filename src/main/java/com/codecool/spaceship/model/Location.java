package com.codecool.spaceship.model;

import lombok.Data;

@Data
public class Location {

    private String name;
    private int distanceFromStation;
    private Resource resource;
}
