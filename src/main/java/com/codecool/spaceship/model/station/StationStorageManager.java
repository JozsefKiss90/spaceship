package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.AbstractStorageManager;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.service.LevelService;

import java.util.HashMap;
import java.util.Map;

public class StationStorageManager extends AbstractStorageManager {

    private static final UpgradeableType type = UpgradeableType.STATION_STORAGE;


    public StationStorageManager(LevelService levelService, int currentLevel, Map<ResourceType, Integer> storedResources) {
        super(levelService, type, currentLevel, storedResources);
    }
    public StationStorageManager(LevelService levelService) {
        this(levelService, 1, new HashMap<>());
    }

//    public int getCurrentCapacity() {
//        return UPGRADE_LEVELS.get(currentLevelIndex).effect();
//    }
//
//    public int getCurrentAvailableStorageSpace() {
//        return getCurrentCapacity() - storedItems.stream().mapToInt(StationResource::getQuantity).sum();
//    }
//
//    public Map<ResourceType, Integer> getStoredItems() {
//        Map<ResourceType, Integer> resources = new HashMap<>();
//        for (StationResource storedItem : storedItems) {
//            resources.put(storedItem.getResourceType(), storedItem.getQuantity());
//        }
//        return resources;
//    }
//
//    public boolean addResource(ResourceType resourceType, int quantity) throws StorageException {
//        if (quantity <= getCurrentAvailableStorageSpace()) {
//            StationResource resource = storedItems.stream()
//                    .filter(sr -> sr.getResourceType() == resourceType)
//                    .findFirst()
//                    .orElse(null);
//            if (resource != null) {
//                resource.setQuantity(resource.getQuantity() + quantity);
//            } else {
//                resource = StationResource.builder()
//                        .resourceType(resourceType)
//                        .quantity(quantity)
//                        .build();
//                storedItems.add(resource);
//            }
//            return true;
//        }
//        throw new StorageException("Not enough storage space");
//    }
//
//    public boolean hasResource(ResourceType resourceType, int quantity) {
//        return storedItems.stream()
//                .anyMatch(stationResource -> stationResource.getResourceType() == resourceType
//                        && stationResource.getQuantity() >= quantity);
//
//    }
//
//    public void removeResource(ResourceType resourceType, int quantity) throws StorageException {
//        if (hasResource(resourceType, quantity)) {
//            StationResource resource = storedItems.stream()
//                    .filter(sr -> sr.getResourceType() == resourceType)
//                    .findFirst()
//                    .orElse(null);
//            resource.setQuantity(resource.getQuantity() - quantity);
//        } else throw new StorageException("Not enough resource");
//    }
//
//    @Override
//    public Map<ResourceType, Integer> getUpgradeCost() throws UpgradeNotAvailableException {
//        if (currentLevelIndex < MAX_LEVEL_INDEX) return new HashMap<>(UPGRADE_LEVELS.get(currentLevelIndex + 1).cost());
//        throw new UpgradeNotAvailableException("Already at max level");
//    }
//
//    @Override
//    public void upgrade() {
//        if (currentLevelIndex < MAX_LEVEL_INDEX) currentLevelIndex++;
//    }
//
//    @Override
//    public int getCurrentLevel() {
//        return currentLevelIndex + 1;
//    }
}
