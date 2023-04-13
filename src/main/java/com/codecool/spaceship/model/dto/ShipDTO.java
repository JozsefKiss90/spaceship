package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.NoSuchPartException;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;

import java.util.Set;
import java.util.stream.Collectors;

public record ShipDTO(int id, String name, String type, Color color, Set<PartDTO> parts, boolean available) {

    public ShipDTO(SpaceShip ship) {
        this(ship.getId(), ship.getName(), getShipType(ship), ship.getColor(),getShipParts(ship), ship.isAvailable());
    }

    private static String getShipType(SpaceShip ship) {
        switch (ship.getClass().getSimpleName()) {
            case "MinerShip" -> {
                return "Miner ship";
            }
            default -> {
                return "Spaceship";
            }
        }
    }

    private static Set<PartDTO> getShipParts(SpaceShip ship) {
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
