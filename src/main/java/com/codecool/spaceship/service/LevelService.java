package com.codecool.spaceship.service;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LevelService {

    private final LevelRepository levelRepository;

    @Autowired
    public LevelService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    public Level getLevelByTypeAndLevel(UpgradeableType type, int level) {
        return levelRepository.getLevelByTypeAndLevel(type, level)
                .orElseThrow(() -> new IllegalArgumentException(type + " has no level " + level + "."));
    }
}
