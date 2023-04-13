package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Set<ShipDTO> getAllShipsFromBase() {
        return shipService.getAllShipsFromBase();
    }
}
