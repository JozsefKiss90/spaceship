package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.MissionDTO;
import com.codecool.spaceship.service.MissionService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/mission")
@CrossOrigin(origins = "http://localhost:3000")
public class MissionController {

    private final MissionService missionService;

    @Autowired
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<MissionDTO>> getAllActiveMissionsForCurrentUser() {
        try {
            return ResponseEntity.ok(missionService.getAllActiveMissionsForCurrentUser());
        } catch (Exception e) {
            return ResponseEntity.of(ControllerExceptionHandler.getProblemDetail(e)).build();
        }
    }

    @GetMapping("/active/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<MissionDTO>> getAllActiveMissionsByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(missionService.getAllActiveMissionsByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.of(ControllerExceptionHandler.getProblemDetail(e)).build();
        }
    }
    @GetMapping("/archived")
    public ResponseEntity<List<MissionDTO>> getAllArchivedMissionsForCurrentUser() {
        return ResponseEntity.ok(missionService.getAllArchivedMissionsForCurrentUser());
    }

    @GetMapping("/archived/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<MissionDTO>> getAllArchivedMissionsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(missionService.getAllArchivedMissionsByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionDTO> getMissionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(missionService.getMissionById(id));
        } catch (Exception e) {
            return ResponseEntity.of(ControllerExceptionHandler.getProblemDetail(e)).build();
        }
    }

    @PostMapping()
    public ResponseEntity<MissionDTO> startNewMission(@RequestBody ObjectNode objectNode) {
        try {
            return ResponseEntity.ok(missionService.startNewMission(
                    objectNode.get("shipId").asLong(),
                    objectNode.get("locationId").asLong(),
                    objectNode.get("activityDuration").asLong()
            ));
        } catch (Exception e) {
            return ResponseEntity.of(ControllerExceptionHandler.getProblemDetail(e)).build();
        }
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<MissionDTO> archiveMission(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(missionService.archiveMission(id));
        } catch (Exception e) {
            return ResponseEntity.of(ControllerExceptionHandler.getProblemDetail(e)).build();
        }
    }
}
