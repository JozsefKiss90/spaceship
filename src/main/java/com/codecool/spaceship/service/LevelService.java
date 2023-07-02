package com.codecool.spaceship.service;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.dto.LevelDTO;
import com.codecool.spaceship.model.dto.NewLevelDTO;
import com.codecool.spaceship.model.exception.DataNotFoundException;
import com.codecool.spaceship.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LevelService {

    private final LevelRepository levelRepository;

    @Autowired
    public LevelService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    public List<UpgradeableType> getLevelTypes() {
        return Arrays.stream(UpgradeableType.values()).toList();
    }

    public Level getLevelByTypeAndLevel(UpgradeableType type, int level) {
        return levelRepository.getLevelByTypeAndLevel(type, level)
                .orElseThrow(() -> new IllegalArgumentException(type + " has no level " + level + "."));
    }

    public List<LevelDTO> getLevelsByType(UpgradeableType type) {
        return levelRepository.getLevelsByType(type).stream()
                .map(LevelDTO::new)
                .toList();
    }

    public LevelDTO updateLevelById(Long id, NewLevelDTO newLevelDTO) {
        Level level = levelRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Level not found"));
        level.setEffect(newLevelDTO.effect());
        level.setCost(newLevelDTO.cost());
        levelRepository.save(level);
        return new LevelDTO(level);
    }
}
