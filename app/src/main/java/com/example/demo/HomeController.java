package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "CI/CD Pipeline deployed successfully!";
    }

    @GetMapping("/health")
    public String health() {
        return "Application is Healthy";
    }

}
