package com.codecool.spaceship;
import com.codecool.spaceship.model.resource.ShipResource;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.resource.StationResource;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationService;
import com.codecool.spaceship.repository.MinerShipRepository;
import com.codecool.spaceship.repository.ShipResourceRepository;
import com.codecool.spaceship.repository.SpaceStationRepository;
import com.codecool.spaceship.repository.StationResourceRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpaceshipApplication {

    public static void main(String[] args) {

		ApplicationContext applicationContext = SpringApplication.run(SpaceshipApplication.class, args);
		ShipResourceRepository shipResourceRepository = applicationContext.getBean(ShipResourceRepository.class);
		StationResourceRepository stationResourceRepository = applicationContext.getBean(StationResourceRepository.class);
		MinerShipRepository minerShipRepository = applicationContext.getBean(MinerShipRepository.class);
		SpaceStationRepository spaceStationRepository = applicationContext.getBean(SpaceStationRepository.class);
		Set<ShipResource> shipResources= new HashSet<>() {{
			add(new ShipResource(1L, ResourceType.METAL, 10));
			add(new ShipResource(2L, ResourceType.CRYSTAL, 5));
		}};
		Set<StationResource> stationResources = new HashSet<>() {{
			add(new StationResource(1L, ResourceType.METAL, 10));
			add(new StationResource(2L, ResourceType.CRYSTAL, 5));
		}};

		shipResources.forEach(resource -> shipResourceRepository.save(resource));
		stationResources.forEach(resource -> stationResourceRepository.save(resource));

		MinerShip minerShip = new MinerShip();
		minerShip.setName("minership");
		minerShip.setColor(Color.DIAMOND);
		minerShip.setEngineLevel(2);
		minerShip.setShieldLevel(3);
		minerShip.setShieldEnergy(100);
		minerShip.setDrillLevel(2);
		minerShip.setResources(shipResources);
		minerShip.setStorageLevel(1);
		minerShipRepository.save(minerShip);

		List<SpaceShip> hangar = new ArrayList<>() {{
			add(minerShip);
		}};

		SpaceStation spaceStation = new SpaceStation();
		spaceStation.setName("spaceStation");
		spaceStation.setStorageLevel(3);
		spaceStation.setHangarLevel(1);
		spaceStation.setHangar(hangar);
		spaceStation.setResources(stationResources);
		spaceStationRepository.save(spaceStation);

	}


}
