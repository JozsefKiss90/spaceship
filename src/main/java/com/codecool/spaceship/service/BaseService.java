package com.codecool.spaceship.service;

import com.codecool.spaceship.model.base.Base;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    private final Base base;

    public BaseService() {
        this.base = new Base("The Base Base");
    }

    public Base getBase() {
        return base;
    }
}
