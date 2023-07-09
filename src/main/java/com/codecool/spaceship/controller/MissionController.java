package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.mission.MissionDTO;
import com.codecool.spaceship.model.dto.mission.MissionDetailDTO;
import com.codecool.spaceship.service.MissionService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/mission")
public class MissionController {

    private final MissionService missionService;

    @Autowired
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/active")
    public List<MissionDTO> getAllActiveMissionsForCurrentUser() {
        return missionService.getAllActiveMissionsForCurrentUser();

    }

    @GetMapping("/active/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<MissionDTO> getAllActiveMissionsByUserId(@PathVariable Long userId) {
        return missionService.getAllActiveMissionsByUserId(userId);

    }

    @GetMapping("/archived")
    public List<MissionDTO> getAllArchivedMissionsForCurrentUser() {
        return missionService.getAllArchivedMissionsForCurrentUser();
    }

    @GetMapping("/archived/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<MissionDTO> getAllArchivedMissionsByUserId(@PathVariable Long userId) {
        return missionService.getAllArchivedMissionsByUserId(userId);
    }

    @GetMapping("/{id}")
    public MissionDetailDTO getMissionById(@PathVariable Long id) {
        return missionService.getMissionById(id);
    }

    @PostMapping("/miner")
    public MissionDetailDTO startNewMinerMission(@RequestBody ObjectNode objectNode) {
        return missionService.startNewMinerMission(
                objectNode.get("shipId").asLong(),
                objectNode.get("locationId").asLong(),
                objectNode.get("activityDuration").asLong()
        );
    }

    @PatchMapping("/{id}/archive")
    public MissionDetailDTO archiveMission(@PathVariable Long id) {
        return missionService.archiveMission(id);
    }

    @PatchMapping("/{id}/abort")
    public MissionDetailDTO abortMission(@PathVariable Long id) {
        return missionService.abortMission(id);
    }
}
