package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.ShipNotFoundException;
import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/ship")
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

    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> updateShipAttributes(@PathVariable int id, @RequestBody ShipDTO shipDTO) {
        try {
            return ResponseEntity.ok(shipService.updateShipAttributes(id, shipDTO.name(), shipDTO.color()));
        } catch (ShipNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
