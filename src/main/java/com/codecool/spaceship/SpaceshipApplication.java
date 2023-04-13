package com.codecool.spaceship;

import com.codecool.spaceship.model.base.Base;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpaceshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpaceshipApplication.class, args);
    }

    @Bean
    public Base base() {
        return new Base("Base One");
    }

}
