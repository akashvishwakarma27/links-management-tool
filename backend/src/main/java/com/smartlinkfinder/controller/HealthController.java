package com.smartlinkfinder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Health Check Controller
 * Provides basic health status without complex dependencies
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    
    /**
     * Basic health check endpoint - no dependencies
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("application", "Smart Link Finder");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Simple detailed health check - no complex monitoring
     */
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("application", "Smart Link Finder");
        response.put("version", "1.0.0");
        response.put("message", "Application is running smoothly");
        
        return ResponseEntity.ok(response);
    }
}
