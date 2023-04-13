package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
