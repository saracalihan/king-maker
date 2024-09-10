package com.example.tahakkum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Main Controller")
public class MainController{

    @GetMapping("/health-check")
    @Operation(summary = "Health check")
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Server is running"),
    })
    public String healthCheck(){
        return "OK";
    }
}
