package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.station.SpaceStation;

public record BaseDTO(String name, HangarDTO hangar, BaseStorageDTO storage) {

    public BaseDTO(SpaceStation base) {
        this(base.getName(), new HangarDTO(base.getHangar()), new BaseStorageDTO(base.getStorage()));
    }

}
