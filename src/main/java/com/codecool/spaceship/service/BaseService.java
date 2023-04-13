package com.codecool.spaceship.service;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.base.Base;
import com.codecool.spaceship.model.ship.SpaceShip;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    private final Base base;

    public BaseService() {
        this.base = new Base("The Base Base");
    }

    public Base getBase() {
        return base;
    }

    public void addResource(Resource resource, int quantity) {
        base.addResource(resource, quantity);
    }

    public void addShip(SpaceShip ship) {
        base.addNewShip(ship);
    }

    public void upgradeStorage() {
        base.upgradeStorage();
    }
}
