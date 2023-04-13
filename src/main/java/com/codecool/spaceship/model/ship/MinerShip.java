package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.Drill;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinerShip extends SpaceShip {

    private static final List<ShipPart> PARTS = List.of(ShipPart.ENGINE, ShipPart.SHIELD, ShipPart.DRILL);
    private static final Map<Resource, Integer> COST = new HashMap<>() {{
        put(Resource.METAL, 50);
        put(Resource.CRYSTAL, 20);
        put(Resource.SILICONE, 20);
    }};
    private final Drill drill;

    public MinerShip(String name, Color color) {
        super(name, color);
        drill = new Drill();
    }

    public int getDrillEfficiency() {
        return drill.getEfficiency();
    }

    @Override
    public List<ShipPart> getPartTypes() {
        return PARTS;
    }

    @Override
    public Upgradeable getPart(ShipPart part) throws NoSuchPartException {
        switch (part) {
            case ENGINE -> {
                return engine;
            }
            case SHIELD -> {
                return shield;
            }
            case DRILL -> {
                return drill;
            }
            default -> throw new NoSuchPartException("No such part on this ship");
        }
    }

    @Override
    public Map<Resource, Integer> getCost() {
        return new HashMap<>(COST);
    }
}
