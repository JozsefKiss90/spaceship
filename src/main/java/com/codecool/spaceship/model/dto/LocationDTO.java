package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.resource.ResourceType;

public record LocationDTO(long id, String name, ResourceType resourceType, int distanceFromStation) {

    public LocationDTO(Location location) {
        this(location.getId(), location.getName(), location.getResourceType(), location.getDistanceFromStation());
    }
}
