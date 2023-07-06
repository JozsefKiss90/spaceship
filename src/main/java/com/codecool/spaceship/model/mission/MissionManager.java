package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.dto.mission.MissionDetailDTO;
import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.ship.SpaceShipManager;

import java.time.Clock;
import java.util.List;

public abstract class MissionManager {

    protected final Mission mission;
    protected final Clock clock;
    protected SpaceShipManager shipManager;

    public MissionManager(Mission mission, Clock clock, SpaceShipManager spaceShipManager) {
        this.mission = mission;
        this.clock = clock;
        if (!mission.getShip().equals(spaceShipManager.getShip())) {
            throw new IllegalArgumentException("Ship is not the one on this mission.");
        }
        this.shipManager = spaceShipManager;
    }

    public void setShipManager(SpaceShipManager spaceShipManager) {
        if (!mission.getShip().equals(spaceShipManager.getShip())) {
            throw new IllegalArgumentException("Ship is not the one on this mission.");
        }
        this.shipManager = spaceShipManager;
    }

    public abstract MissionDetailDTO getDetailedDTO();

    public abstract boolean updateStatus();

    public abstract boolean abortMission() throws IllegalOperationException;

    public boolean archiveMission() throws IllegalOperationException {
        if (mission.getCurrentStatus() == MissionStatus.ARCHIVED) {
            throw new IllegalOperationException("Mission is already archived");
        } else if (mission.getCurrentStatus() != MissionStatus.OVER) {
            throw new IllegalOperationException("Mission can't be archived until its over");
        } else {
            mission.setCurrentStatus(MissionStatus.ARCHIVED);
            return true;
        }
    }

    private void generateEnRouteEvents() {
        //TODO add pirateAttack/Meteor Storm based on chance
        //else event for arrival at destination gets added
        EventType travelEventType = (mission.getCurrentStatus() == MissionStatus.EN_ROUTE)
                ? EventType.ARRIVAL_AT_LOCATION
                : EventType.RETURNED_TO_STATION;
        Event travelEvent = Event.builder()
                .endTime(mission.getCurrentObjectiveTime())
                .eventType(travelEventType)
                .build();
        pushNewEvent(travelEvent);
    }

    protected static long calculateTravelDurationInSecs(SpaceShipManager ship, int distanceFromBase) {
        double speedInDistancePerHour = ship.getSpeed();
        int hourToSec = 60 * 60;
        return (long) (distanceFromBase / speedInDistancePerHour * hourToSec);
    }

    protected Event peekLastEvent() {
        List<Event> events = mission.getEvents();
        return events.get(events.size() - 1);
    }

    protected boolean pushNewEvent(Event event) {
        List<Event> events = mission.getEvents();
        return events.add(event);
    }

    protected Event popLastEvent() {
        List<Event> events = mission.getEvents();
        Event event = events.get(events.size() - 1);
        events.remove(event);
        return event;
    }
}
