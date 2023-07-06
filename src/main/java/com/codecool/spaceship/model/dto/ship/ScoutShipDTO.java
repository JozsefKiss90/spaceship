package com.codecool.spaceship.model.dto.ship;

import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.shipparts.Color;
import lombok.Getter;

@Getter
public class ScoutShipDTO extends ShipDetailDTO{

    private final int scannerLevel;
    private final int scannerEfficiency;

    public ScoutShipDTO(long id, String name, Color color, ShipType type, Mission mission, int engineLevel, int maxSpeed,
                        int shieldLevel, int shieldEnergy, int maxShieldEnergy, int scannerLevel, int scannerEfficiency) {
        super(id, name, color, type, mission, engineLevel, maxSpeed, shieldLevel, shieldEnergy, maxShieldEnergy);
        this.scannerLevel = scannerLevel;
        this.scannerEfficiency = scannerEfficiency;
    }
}
