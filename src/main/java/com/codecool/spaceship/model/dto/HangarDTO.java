package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.station.HangarService;

import java.util.Set;
import java.util.stream.Collectors;

public record HangarDTO(Set<ShipDTO> ships, int level, int capacity, int freeDocks) {

    public HangarDTO(HangarService hangarService) {
        this(getAllShipDtos(hangarService), hangarService.getCurrentLevel(), hangarService.getCurrentCapacity(), hangarService.getCurrentAvailableDocks());
    }

    public static Set<ShipDTO> getAllShipDtos(HangarService hangarService) {
        return hangarService.getAllShips().stream().map(ShipDTO::new).collect(Collectors.toSet());
    }
}
