package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/base")
public class SpaceStationController {

    private final BaseService baseService;

    @Autowired
    public SpaceStationController(BaseService baseService) {
        this.baseService = baseService;
    }

    @GetMapping()
    public ResponseEntity<SpaceStationDTO> getBase() {
        return ResponseEntity.ok(baseService.getBase().get());
    }

    /*
    @GetMapping()
    public ResponseEntity<SpaceStationDTO> getBase() {
        return ResponseEntity.ok(new SpaceStationDTO(baseService.getBase()));
    }

    @PostMapping("/add/resources")
    public ResponseEntity<Boolean> addResource(@RequestBody Map<Resource, Integer> resources) {
        for (Resource resource : resources.keySet()) {
            try {
                baseService.addResource(resource, resources.get(resource));
            } catch (StorageException e) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
            }
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/add/ship")
    public ResponseEntity<Boolean> addShip(@RequestBody ObjectNode objectNode) {
        try {
            return ResponseEntity.ok(baseService.addShip(new MinerShipService(objectNode.get("name").asText(), Color.valueOf(objectNode.get("color").asText()))));
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
    public ResponseEntity<Boolean> upgradeHangar() {
        try {
            return ResponseEntity.ok(baseService.upgradeHangar());
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

     */
}
