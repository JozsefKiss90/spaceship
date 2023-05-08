package com.codecool.spaceship.model.dto;


import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;

public record ShipDTO(Long id, String name, Color color, int engineLevel, int shieldLevel, int shieldEnergy) {

    public ShipDTO(SpaceShip ship) {
        this(ship.getId(), ship.getName(), ship.getColor(), ship.getEngineLevel(), ship.getShieldLevel(), ship.getEngineLevel());
    }

    /*private static String getShipType(SpaceShipService ship) {
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
    }*/
}
