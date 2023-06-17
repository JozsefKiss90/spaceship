package com.codecool.spaceship.model.dto;


import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;

public record ShipDTO(Long id, String name, Color color, ShipType type, long missionId, int engineLevel, int shieldLevel, int shieldEnergy) {

    public ShipDTO(SpaceShip ship) {
        this(ship.getId(), ship.getName(), ship.getColor(), getShipType(ship), getCurrentMissionId(ship.getCurrentMission()), ship.getEngineLevel(), ship.getShieldLevel(), ship.getShieldEnergy());
    }

    private static ShipType getShipType(SpaceShip ship) {
        if (ship instanceof MinerShip) {
            return ShipType.MINER;
        } else {
            return null;
        }
    }

    private static Long getCurrentMissionId(Mission mission) {
        if (mission == null) {
            return 0L;
        } else {
            return mission.getId();
        }
    }
}
