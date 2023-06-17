package com.codecool.spaceship.service;

import com.codecool.spaceship.model.dto.LocationDTO;
import com.codecool.spaceship.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> getAllLocationsByUser(Long userId) {
        return locationRepository.findAll().stream()
                .map(LocationDTO::new)
                .toList();
    }
}
