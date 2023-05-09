package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.HangarDTO;
import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.dto.SpaceStationStorageDTO;
import com.codecool.spaceship.model.exception.ShipNotFoundException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.service.StationService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/base")
@CrossOrigin(origins = "http://localhost:3000")
public class SpaceStationController {

    private final StationService stationService;

    @Autowired
    public SpaceStationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping({"/{baseId}"})
    public ResponseEntity<SpaceStationDTO> getBaseById(@PathVariable long baseId) {
        try {
            return ResponseEntity.ok(stationService.getBaseById(baseId));
        } catch (Exception e) {
            return ResponseEntity.of(getProblemDetail(e)).build();
        }
    }

    @PostMapping("/{baseId}/add/resources")
    public ResponseEntity<Boolean> addResources(@PathVariable long baseId, @RequestBody Map<ResourceType, Integer> resources) {
        try {
            return ResponseEntity.ok(stationService.addResources(baseId, resources));
        } catch (Exception e) {
            return ResponseEntity.of(getProblemDetail(e)).build();
        }
    }

    @PostMapping("/{baseId}/add/ship")
    public ResponseEntity<Boolean> addShip(@PathVariable long baseId, @RequestBody ObjectNode objectNode) {
        try {
            return ResponseEntity.ok(stationService.addShip(baseId,
                    objectNode.get("name").asText(),
                    Color.valueOf(objectNode.get("color").asText().toUpperCase()),
                    ShipType.valueOf(objectNode.get("type").asText().toUpperCase())));
        } catch (Exception e) {
            return ResponseEntity.of(getProblemDetail(e)).build();
        }
    }

    @GetMapping("/{baseId}/storage")
    public ResponseEntity<SpaceStationStorageDTO> getStationStorage(@PathVariable long baseId) {
        try {
            return ResponseEntity.ok(stationService.getStationStorage(baseId));
        } catch (Exception e) {
            return ResponseEntity.of(getProblemDetail(e)).build();
        }
    }

    @GetMapping("/{baseId}/storage/resources")
    public ResponseEntity<Map<ResourceType, Integer>> getStoredResources(@PathVariable long baseId) {
        try {
            return ResponseEntity.ok(stationService.getStoredResources(baseId));
        } catch (Exception e) {
            return ResponseEntity.of(getProblemDetail(e)).build();
        }
    }

    @GetMapping("/{baseId}/storage/upgrade")
    public ResponseEntity<Map<ResourceType, Integer>> getStorageUpgradeCost(@PathVariable long baseId) {
        try {
            return ResponseEntity.ok(stationService.getStorageUpgradeCost(baseId));
        } catch (Exception e) {
            return ResponseEntity.of(getProblemDetail(e)).build();
        }
    }

    @PostMapping("/{baseId}/storage/upgrade")
    public ResponseEntity<Boolean> upgradeStorage(@PathVariable long baseId) {
        try {
            return ResponseEntity.ok(stationService.upgradeStorage(baseId));
        } catch (Exception e) {
            return ResponseEntity.of(getProblemDetail(e)).build();
        }
    }

    @GetMapping("/{baseId}/hangar")
    public ResponseEntity<HangarDTO> getStationHangar(@PathVariable long baseId) {
        try {
            return ResponseEntity.ok(stationService.getStationHangar(baseId));
        } catch (Exception e) {
            return ResponseEntity.of(getProblemDetail(e)).build();
        }
    }

    @GetMapping("/{baseId}/hangar/upgrade")
    public ResponseEntity<Map<ResourceType, Integer>> getHangarUpgradeCost(@PathVariable long baseId) {
        try {
            return ResponseEntity.ok(stationService.getHangarUpgradeCost(baseId));
        } catch (Exception e) {
            return ResponseEntity.of(getProblemDetail(e)).build();
        }
    }

    @PostMapping("/{baseId}/hangar/upgrade")
    public ResponseEntity<Boolean> upgradeHangar(@PathVariable long baseId) {
        try {
            return ResponseEntity.ok(stationService.upgradeHangar(baseId));
        } catch (Exception e) {
            return ResponseEntity.of(getProblemDetail(e)).build();
        }
    }

    private ProblemDetail getProblemDetail(Exception e) {
        if (e instanceof SecurityException) {
            return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
        } else if (e instanceof ShipNotFoundException){
            return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage());
        } else {
            return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage());
        }
    }

}
