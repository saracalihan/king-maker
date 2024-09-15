package com.saracalihan.tahakkum.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/app")
public class ApplicationController {

    @PostMapping
    void create(){
    }

    
}
