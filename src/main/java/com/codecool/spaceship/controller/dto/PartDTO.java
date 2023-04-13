package com.codecool.spaceship.controller.dto;

import com.codecool.spaceship.model.Upgradeable;

public record PartDTO(String name, int level) {

    public PartDTO(Upgradeable part) {
        this(part.getClass().getSimpleName(), part.getCurrentLevel());
    }
}
