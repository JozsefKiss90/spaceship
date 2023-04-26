package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.dto.CostDTO;
import com.codecool.spaceship.model.exception.ShipNotFoundException;
import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import com.codecool.spaceship.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/ship")
@CrossOrigin("http://localhost:3000")
public class ShipController {

    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }


    @GetMapping("/all")
    public ResponseEntity<Set<ShipDTO>> getAllShipsFromBase() {
        return ResponseEntity.ok(shipService.getAllShipsFromBase());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipDTO> getShipById(@PathVariable int id) {
        Optional<ShipDTO> ship = shipService.getShipByID(id);
        return ship
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/upgrade")
    public ResponseEntity<Boolean> upgradeShipPart(@RequestParam int id, @RequestParam ShipPart part) {
        try {
            return ResponseEntity.ok(shipService.upgradeShip(id, part));
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

    @GetMapping("/cost/miner")
    public ResponseEntity<CostDTO> getMinerShipCost() {
        return ResponseEntity.ok(shipService.getMinerShipCost());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> updateShipAttributes(@PathVariable int id, @RequestBody ShipDTO shipDTO) {
        try {
            return ResponseEntity.ok(shipService.updateShipAttributes(id, shipDTO.name(), shipDTO.color()));
        } catch (ShipNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteShipById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(shipService.deleteShip(id));
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }
}
