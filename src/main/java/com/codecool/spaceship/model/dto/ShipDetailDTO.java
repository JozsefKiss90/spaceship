package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.shipparts.Color;
import lombok.*;

@Getter
public abstract class ShipDetailDTO extends ShipDTO {
    private final int engineLevel;
    private final int maxSpeed;
    private final int shieldLevel;
    private final int shieldEnergy;
    private final int maxShieldEnergy;

    public ShipDetailDTO(long id, String name, Color color, ShipType type, Mission mission, int engineLevel, int maxSpeed, int shieldLevel, int shieldEnergy, int maxShieldEnergy) {
        super(id, name, color, type, mission);
        this.engineLevel = engineLevel;
        this.maxSpeed = maxSpeed;
        this.shieldLevel = shieldLevel;
        this.shieldEnergy = shieldEnergy;
        this.maxShieldEnergy = maxShieldEnergy;
    }

}
