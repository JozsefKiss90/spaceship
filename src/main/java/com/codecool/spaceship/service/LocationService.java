package com.codecool.spaceship.service;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.dto.LocationDTO;
import com.codecool.spaceship.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> getAllLocationsForCurrentUser() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return locationRepository.findAllByUser(user).stream()
                .map(LocationDTO::new)
                .toList();
    }
}
