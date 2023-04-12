package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.Drill;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;

import java.util.List;

public class MinerShip extends SpaceShip {

    private final Drill drill;

    public MinerShip(String name, Color color) {
        super(name, color);
        drill = new Drill();
    }

    public int getDrillEfficiency() {
        return drill.getEfficiency();
    }

    @Override
    public List<ShipPart> getParts() {
        return List.of(ShipPart.CARGO, ShipPart.SHIELD, ShipPart.DRILL);
    }

    @Override
    public Upgradeable getPart(ShipPart part) throws Exception {
        switch (part) {
            case ENGINE ->
            {
                return engine;
            }
            case SHIELD -> {
                return shield;
            }
            case DRILL -> {
                return drill;
            }
            default -> throw new Exception("No such part on this ship");
        }
    }
}
