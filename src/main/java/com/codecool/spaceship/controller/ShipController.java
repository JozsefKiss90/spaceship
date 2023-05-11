package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.MinerShipDTO;
import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.model.exception.DataNotFoundException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import com.codecool.spaceship.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/ship")
@CrossOrigin(origins = "http://localhost:3000")
public class ShipController {

    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("/all/{stationId}")
    public ResponseEntity<List<ShipDTO>> getAllShipsFromBase(@PathVariable Long stationId) {
        return ResponseEntity.ok(shipService.getShipsByStation(stationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipDTO> getShipById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(shipService.getShipByID(id));
        } catch (DataNotFoundException e){
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage())).build();
        }
    }

    @GetMapping("/miner/{id}")
    public ResponseEntity<MinerShipDTO> getMinerShipById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(shipService.getMinerShipById(id));
        } catch (DataNotFoundException e){
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage())).build();
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }
    @GetMapping("/miner/{id}/upgrade")
    public ResponseEntity<Map<ResourceType, Integer>> getMinderShipShipPartUpgradeCost(@PathVariable Long id, @RequestParam ShipPart part) {
        try {
            return ResponseEntity.ok(shipService.getShipPartUpgradeCost(id,part));
        } catch (ShipNotFoundException e){
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage())).build();
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

    @PatchMapping("/miner/{id}/upgrade")
    public ResponseEntity<MinerShipDTO> upgradeMinerShipShipPart(@PathVariable Long id, @RequestParam ShipPart part) {
        try {
            return ResponseEntity.ok(shipService.upgradeMinerShip(id, part));
        } catch (DataNotFoundException e){
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage())).build();
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

    @GetMapping("/color")
    public ResponseEntity<Color[]> getAvailableColors() {
        return ResponseEntity.ok(shipService.getColors());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ShipDTO> updateShipAttributes(@PathVariable Long id, @RequestBody ShipDTO shipDTO) {
        try {
            return ResponseEntity.ok(shipService.updateShipAttributes(id, shipDTO.name(), shipDTO.color()));
        } catch (DataNotFoundException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage())).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteShipById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(shipService.deleteShipById(id));
        } catch (DataNotFoundException e){
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage())).build();
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

    @GetMapping("/cost/{shipType}")
    public ResponseEntity<Map<ResourceType, Integer>> getMinerShipCost(@PathVariable String shipType) {
        try {
            return ResponseEntity.ok(shipService.getShipCost(ShipType.valueOf(shipType.toUpperCase())));
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

}
