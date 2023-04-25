package com.codecool.spaceship.model.ship;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
public class MinerShip extends SpaceShip {
    private int drillLevel;
}
