package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.dto.mission.MissionDetailDTO;
import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.ship.ScoutShipManager;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ScoutingMissionManager extends MissionManager{

    public ScoutingMissionManager(Mission mission, Clock clock, ScoutShipManager scoutShipManager) {
        super(mission, clock, scoutShipManager);
    }

    public ScoutingMissionManager(Mission mission, ScoutShipManager scoutShipManager) {
        super(mission, Clock.systemUTC(), scoutShipManager);
    }

    public static Mission startScoutingMission(ScoutShipManager scoutShipManager, int distance, long activityDurationInSecs, Clock clock) throws IllegalOperationException {
        if (!scoutShipManager.isAvailable()) {
            throw new IllegalOperationException("This ship is already on a mission");
        }
        LocalDateTime startTime = LocalDateTime.now(clock);
        long travelDurationInSecs = calculateTravelDurationInSecs(scoutShipManager, distance);
        long approxMissionDurationInSecs = travelDurationInSecs * 2 + activityDurationInSecs;

        Mission mission = MiningMission.builder()
                .startTime(startTime)
                .activityDurationInSecs(activityDurationInSecs)
                .travelDurationInSecs(travelDurationInSecs)
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(startTime.plusSeconds(travelDurationInSecs))
                .approxEndTime(startTime.plusSeconds(approxMissionDurationInSecs))
                .missionType(MissionType.SCOUTING)
                .ship(scoutShipManager.getShip())
                .user(scoutShipManager.getShip().getUser())
                .events(new ArrayList<>())
                .build();
        scoutShipManager.setCurrentMission(mission);
        return mission;
    }

    @Override
    public MissionDetailDTO getDetailedDTO() {
        return null;
    }

    @Override
    public boolean updateStatus() {
        return false;
    }

    @Override
    public boolean abortMission() throws IllegalOperationException {
        return false;
    }
}
