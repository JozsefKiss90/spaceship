package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.BaseDTO;
import com.codecool.spaceship.model.base.Base;
import com.codecool.spaceship.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base")
public class BaseController {

    private BaseService baseService;

    @Autowired
    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }

    @GetMapping("/")
    public ResponseEntity<BaseDTO> getBase() {
        return ResponseEntity.ok(new BaseDTO(baseService.getBase()));
    }


}
