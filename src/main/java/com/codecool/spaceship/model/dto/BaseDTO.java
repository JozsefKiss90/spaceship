package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.base.Base;

public record BaseDTO(String name, HangarDTO hangar, BaseStorageDTO storage) {

    public BaseDTO(Base base) {
        this(base.getName(), new HangarDTO(base.getHangar()), new BaseStorageDTO(base.getStorage()));
    }

}
