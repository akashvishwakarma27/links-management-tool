package com.smartlinkfinder.controller;

import com.smartlinkfinder.service.HealthMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller for monitoring and error prevention
 * Provides endpoints for system health monitoring
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    
    @Autowired
    private HealthMonitoringService healthMonitoringService;
    
    /**
     * Basic health check endpoint
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("application", "Smart Link Finder");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Detailed health check endpoint
     */
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("application", "Smart Link Finder");
        response.put("version", "1.0.0");
        response.put("systemHealth", healthMonitoringService.getSystemHealth());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Manual health check trigger
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> triggerHealthCheck() {
        boolean isHealthy = healthMonitoringService.triggerManualHealthCheck();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", isHealthy ? "UP" : "DOWN");
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("manualCheck", isHealthy);
        response.put("systemHealth", healthMonitoringService.getSystemHealth());
        
        return ResponseEntity.ok(response);
    }
}
