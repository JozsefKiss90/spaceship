package com.codecool.spaceship;
import com.codecool.spaceship.model.resource.Resource;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStationService;
import com.codecool.spaceship.repository.MinerShipRepository;
import com.codecool.spaceship.repository.ResourceRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;




@SpringBootApplication
public class SpaceshipApplication {

    public static void main(String[] args) {

		ApplicationContext applicationContext = SpringApplication.run(SpaceshipApplication.class, args);
		ResourceRepository resourceRepository = applicationContext.getBean(ResourceRepository.class);
		MinerShipRepository minerShipRepository = applicationContext.getBean(MinerShipRepository.class);
		Set<Resource> resources = new HashSet<>() {{
			add(new Resource(1L, ResourceType.METAL, 10));
			add(new Resource(2L, ResourceType.CRYSTAL, 5));
		}};

		resources.forEach(resource -> resourceRepository.save(resource));

		MinerShip minerShip = new MinerShip();
		minerShip.setName("minership");
		minerShip.setColor(Color.DIAMOND);
		minerShip.setEngineLevel(2);
		minerShip.setShieldLevel(3);
		minerShip.setShieldEnergy(100);
		minerShip.setDrillLevel(2);
		minerShip.setResources(resources);
		minerShip.setStorageLevel(1);

		minerShipRepository.save(minerShip);


	}

	@Bean
	public SpaceStationService base() {
		return new SpaceStationService("Base One");
	}
}
