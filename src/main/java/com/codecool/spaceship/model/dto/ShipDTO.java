package com.codecool.spaceship.model.dto;


import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.ship.SpaceShipService;
import com.codecool.spaceship.model.ship.shipparts.Color;

import java.util.Set;
import java.util.stream.Collectors;

public record ShipDTO(Long id, String name, String type, Color color, Set<PartDTO> parts, boolean available) {

    public ShipDTO(SpaceShipService ship) {
        this(ship.getId(), ship.getName(), getShipType(ship), ship.getColor(),getShipParts(ship), ship.isAvailable());
    }

    private static String getShipType(SpaceShipService ship) {
        switch (ship.getClass().getSimpleName()) {
            case "MinerShip" -> {
                return "Miner ship";
            }
            default -> {
                return "Spaceship";
            }
        }
    }

    private static Set<PartDTO> getShipParts(SpaceShipService ship) {
        return ship.getPartTypes().stream()
                .map(shipPart -> {
                    try {
                        return new PartDTO(ship.getPart(shipPart));
                    } catch (NoSuchPartException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
    }
}
