package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.dto.BaseDTO;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.service.BaseService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void addResource(@RequestBody Map<Resource,Integer> resource) {
        resource.entrySet().forEach(entry->baseService.addResource(entry.getKey(),entry.getValue()));
    }

    @PostMapping("/add/ship")
    public void addShip(@RequestBody ObjectNode objectNode) {
        baseService.addShip(new MinerShip(objectNode.get("name").asText(),Color.valueOf(objectNode.get("color").asText())));
    }

    @PostMapping("/upgrade/storage")
    public void upgradeStorage() {
        baseService.upgradeStorage();
    }


}
