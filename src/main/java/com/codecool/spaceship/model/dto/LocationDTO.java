package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.resource.ResourceType;

public record LocationDTO(long id, String name, ResourceType resourceType, int distanceFromStation, long missionId) {

    public LocationDTO(Location location) {
        this(location.getId(), location.getName(), location.getResourceType(), location.getDistanceFromStation(),getMissionId(location.getCurrentMission()));
    }

    private static Long getMissionId(Mission mission) {
        if (mission == null) {
            return 0L;
        } else  return mission.getId();
    }
}
