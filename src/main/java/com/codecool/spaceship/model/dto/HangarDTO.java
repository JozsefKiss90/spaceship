package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.station.Hangar;

import java.util.Set;
import java.util.stream.Collectors;

public record HangarDTO(Set<ShipDTO> ships, int level, int capacity, int freeDocks) {

    public HangarDTO(Hangar hangar) {
        this(getAllShipDtos(hangar), hangar.getCurrentLevel(), hangar.getCurrentCapacity(), hangar.getCurrentAvailableDocks());
    }

    public static Set<ShipDTO> getAllShipDtos(Hangar hangar) {
        return hangar.getAllShips().stream().map(ShipDTO::new).collect(Collectors.toSet());
    }
}
