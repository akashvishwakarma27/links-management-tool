package com.smartlinkfinder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check and Root Controller
 * Provides basic health status and root endpoints
 * Note: Application runs on /api context path
 */
@RestController
public class HealthController {
    
    /**
     * Root endpoint (relative to context path) - eliminates 404 error
     * Maps to: https://links-management-tool.onrender.com/api/
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "LMT Smart Link Finder API");
        response.put("version", "1.0.0");
        response.put("timestamp", Instant.now().toString());
        response.put("context_path", "/api");
        response.put("endpoints", new String[]{
            "/health - Health check",
            "/auth/login - Authentication",
            "/links/search - Search links",
            "/links - Link management"
        });
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Health check endpoint - primary health monitoring
     * Maps to: https://links-management-tool.onrender.com/api/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("application", "LMT Smart Link Finder");
        response.put("version", "1.0.0");
        response.put("service", "Links Management Tool");
        response.put("context_path", "/api");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Simple detailed health check - no complex monitoring
     * Maps to: https://links-management-tool.onrender.com/api/health/detailed
     */
    @GetMapping("/health/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("application", "LMT Smart Link Finder");
        response.put("version", "1.0.0");
        response.put("service", "Links Management Tool");
        response.put("context_path", "/api");
        response.put("message", "Application is running smoothly");
        response.put("uptime", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
