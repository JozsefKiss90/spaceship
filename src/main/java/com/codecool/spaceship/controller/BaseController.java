package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.dto.BaseDTO;
import com.codecool.spaceship.model.exception.ShipNotFoundException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.service.BaseService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/base")
public class BaseController {

    private final BaseService baseService;

    @Autowired
    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }

    @GetMapping()
    public ResponseEntity<BaseDTO> getBase() {
        return ResponseEntity.ok(new BaseDTO(baseService.getBase()));
    }

    @PostMapping("/add/resources")
    public ResponseEntity<Boolean> addResource(@RequestBody Map<Resource, Integer> resource) {
        try {
            resource.forEach((key, value) -> {
                try {
                    baseService.addResource(key, value);
                } catch (StorageException e) {
                    throw new RuntimeException(e);
                }
            });
            return ResponseEntity.ok(true);
        } catch (RuntimeException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

    @PostMapping("/add/ship")
    public ResponseEntity<Boolean> addShip(@RequestBody ObjectNode objectNode) {
        try {
            return ResponseEntity.ok(baseService.addShip(new MinerShip(objectNode.get("name").asText(), Color.valueOf(objectNode.get("color").asText()))));
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

    @PostMapping("/upgrade/storage")
    public ResponseEntity<Boolean> upgradeStorage() {
        try {
            return ResponseEntity.ok(baseService.upgradeStorage());
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

    @PostMapping("/upgrade/hangar")
    public ResponseEntity<Boolean>  upgradeHangar() {
        try {
            return ResponseEntity.ok(baseService.upgradeHangar());
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }
}
