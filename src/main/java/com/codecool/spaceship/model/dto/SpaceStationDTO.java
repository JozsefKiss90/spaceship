package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.station.SpaceStationService;

public record SpaceStationDTO(String name, HangarDTO hangar, SpaceStationStorageDTO storage) {

    public SpaceStationDTO(SpaceStationService base) {
        this(base.getName(), new HangarDTO(base.getHangar()), new SpaceStationStorageDTO(base.getStorage()));
    }

}
