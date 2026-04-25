package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portinfo")
public class InfoController {

    @Value("8080")
    private String serverPort;

    @GetMapping("/port")
    public String getPort() {
        return "Application is running on port: " + serverPort;
    }
}
