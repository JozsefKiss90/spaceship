package com.codecool.spaceship.model;

import com.codecool.spaceship.model.resource.ResourceType;
import lombok.Data;

@Data
public class Location {

    private String name;
    private int distanceFromStation;
    private ResourceType resourceType;
}
