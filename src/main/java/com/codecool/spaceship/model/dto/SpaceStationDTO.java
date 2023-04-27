package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.resource.StationResource;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.station.SpaceStation;

import java.util.Set;

public record SpaceStationDTO(Long id, String name, int storageLevel, Set<SpaceShip> hangar, Set<StationResource> resources) {
    public SpaceStationDTO(SpaceStation base) {
        this(base.getId(), base.getName(), base.getStorageLevelIndex(),base.getHangar(), base.getResources());
    }

 /*
    public SpaceStationDTO(SpaceStationService base) {
        this(base.getName(), new HangarDTO(base.getHangar()), new SpaceStationStorageDTO(base.getStorage()));
    }
*/
}
