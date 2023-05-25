package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.LocationDTO;
import com.codecool.spaceship.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/location")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping()
    public ResponseEntity<List<LocationDTO>> getAllLocationsByUser(@RequestParam Long user) {
        return ResponseEntity.ok(locationService.getAllLocationsByUser(user));
    }
}
