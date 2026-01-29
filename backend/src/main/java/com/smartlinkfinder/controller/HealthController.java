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
 */
@RestController
public class HealthController {
    
    /**
     * Root endpoint - eliminates 404 error
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "LMT Smart Link Finder API");
        response.put("version", "1.0.0");
        response.put("timestamp", Instant.now().toString());
        response.put("endpoints", new String[]{
            "/health - Health check",
            "/api/auth/login - Authentication",
            "/api/links/search - Search links",
            "/api/links - Link management"
        });
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * API base endpoint - provides API information
     */
    @GetMapping("/api")
    public ResponseEntity<Map<String, Object>> apiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "LMT Smart Link Finder API");
        response.put("version", "1.0.0");
        response.put("status", "RUNNING");
        response.put("timestamp", Instant.now().toString());
        response.put("description", "Links Management Tool API for smart link management");
        response.put("available_endpoints", new String[]{
            "POST /api/auth/login - User authentication",
            "GET /api/links/search - Search links",
            "GET /api/links - Get all links (authenticated)",
            "POST /api/links - Create link (authenticated)",
            "PUT /api/links/{id} - Update link (authenticated)",
            "DELETE /api/links/{id} - Delete link (authenticated)"
        });
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Basic health check endpoint - no dependencies
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("application", "LMT Smart Link Finder");
        response.put("version", "1.0.0");
        response.put("service", "Links Management Tool");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Simple detailed health check - no complex monitoring
     */
    @GetMapping("/health/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("application", "LMT Smart Link Finder");
        response.put("version", "1.0.0");
        response.put("service", "Links Management Tool");
        response.put("message", "Application is running smoothly");
        response.put("uptime", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
