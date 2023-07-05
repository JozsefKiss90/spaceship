package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.ShipManagerFactory;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.SpaceShipManager;
import org.springframework.stereotype.Component;

@Component
public class MissionFactory {

    private final ShipManagerFactory shipManagerFactory;

    public MissionFactory(ShipManagerFactory shipManagerFactory) {
        this.shipManagerFactory = shipManagerFactory;
    }

    public Mission startNewMission(SpaceShip spaceShip, Location location, long activityDurationInSecs) {
        SpaceShipManager spaceShipManager = shipManagerFactory.getSpaceShipManager(spaceShip);
        if (spaceShipManager instanceof MinerShipManager) {
            return MissionManager.startMiningMission((MinerShipManager) spaceShipManager, location, activityDurationInSecs);
        } else {
            throw new RuntimeException("Ship type is not recognized");
        }
    }
    public MissionManager getMissionManager(Mission mission) {
        SpaceShipManager spaceShipManager = shipManagerFactory.getSpaceShipManager(mission.getShip());
        if (spaceShipManager instanceof MinerShipManager) {
            return new MissionManager(mission, (MinerShipManager) spaceShipManager);
        } else {
            throw new RuntimeException("Ship type is not recognized");
        }
    }
}
