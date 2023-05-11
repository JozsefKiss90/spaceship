package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.MissionDTO;
import com.codecool.spaceship.model.exception.DataNotFoundException;
import com.codecool.spaceship.service.MissionService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mission")
@CrossOrigin(origins = "http://localhost:3000")
public class MissionController {

    private final MissionService missionService;

    @Autowired
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/all/{userId}/active")
    public ResponseEntity<List<MissionDTO>> getAllActiveMissionsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(missionService.getAllActiveMissionsByUserId(userId));
    }

    @GetMapping("/all/{userId}/archived")
    public ResponseEntity<List<MissionDTO>> getAllArchivedMissionsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(missionService.getAllArchivedMissionsByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionDTO> getMissionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(missionService.getMissionById(id));
        } catch (DataNotFoundException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage())).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<MissionDTO> startNewMission(@RequestBody ObjectNode objectNode) {
        try {
            return ResponseEntity.ok(missionService.startNewMission(
                    objectNode.get("shipId").asLong(),
                    objectNode.get("locationId").asLong(),
                    objectNode.get("activityDuration").asLong()
            ));
        } catch (DataNotFoundException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage())).build();
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<Boolean> archiveMission(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(missionService.archiveMission(id));
        } catch (DataNotFoundException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage())).build();
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }
}
