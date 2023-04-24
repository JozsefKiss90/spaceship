package com.codecool.spaceship;

import com.codecool.spaceship.model.station.SpaceStation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpaceshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpaceshipApplication.class, args);
    }

	@Bean
	public SpaceStation base() {
		return new SpaceStation("Base One");
	}
}
