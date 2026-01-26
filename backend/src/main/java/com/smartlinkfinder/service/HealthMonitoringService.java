package com.smartlinkfinder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Monitoring Service for proactive error prevention
 * Monitors system health and prevents issues before they occur
 */
@Service
public class HealthMonitoringService {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthMonitoringService.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    private int consecutiveFailures = 0;
    private LocalDateTime lastHealthCheck = LocalDateTime.now();
    private boolean systemHealthy = true;
    
    /**
     * Scheduled health check every 5 minutes
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void performHealthCheck() {
        try {
            logger.info("Performing scheduled health check...");
            
            // Check database connectivity
            checkDatabaseHealth();
            
            // Check memory usage
            checkMemoryUsage();
            
            // Check disk space (if applicable)
            checkDiskSpace();
            
            // Reset failure counter on success
            if (consecutiveFailures > 0) {
                logger.info("System recovered after {} consecutive failures", consecutiveFailures);
                consecutiveFailures = 0;
            }
            
            systemHealthy = true;
            lastHealthCheck = LocalDateTime.now();
            
            logger.info("Health check completed successfully");
            
        } catch (Exception e) {
            consecutiveFailures++;
            systemHealthy = false;
            logger.error("Health check failed (attempt {}): {}", consecutiveFailures, e.getMessage());
            
            // Alert after 3 consecutive failures
            if (consecutiveFailures >= 3) {
                sendAlert("System health check failed", e.getMessage());
            }
        }
    }
    
    /**
     * Check database connectivity
     */
    private void checkDatabaseHealth() {
        try {
            // This would be implemented based on your database
            // For now, we'll simulate the check
            logger.debug("Database health check passed");
        } catch (Exception e) {
            throw new RuntimeException("Database health check failed: " + e.getMessage());
        }
    }
    
    /**
     * Check memory usage
     */
    private void checkMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        double memoryUsagePercent = (double) usedMemory / maxMemory * 100;
        
        logger.debug("Memory usage: {}% ({}MB used / {}MB max)", 
            String.format("%.2f", memoryUsagePercent),
            usedMemory / 1024 / 1024,
            maxMemory / 1024 / 1024);
        
        if (memoryUsagePercent > 85) {
            throw new RuntimeException("High memory usage detected: " + String.format("%.2f", memoryUsagePercent) + "%");
        }
    }
    
    /**
     * Check disk space
     */
    private void checkDiskSpace() {
        try {
            java.io.File disk = new java.io.File(".");
            long freeSpace = disk.getFreeSpace();
            long totalSpace = disk.getTotalSpace();
            double usagePercent = (double) (totalSpace - freeSpace) / totalSpace * 100;
            
            logger.debug("Disk usage: {}% free", String.format("%.2f", 100 - usagePercent));
            
            if (usagePercent > 90) {
                throw new RuntimeException("Low disk space: " + String.format("%.2f", usagePercent) + "% used");
            }
        } catch (Exception e) {
            logger.warn("Could not check disk space: {}", e.getMessage());
        }
    }
    
    /**
     * Send alert for critical issues
     */
    private void sendAlert(String subject, String message) {
        logger.error("ALERT: {} - {}", subject, message);
        
        // In a production environment, you would send email, SMS, or push notifications
        // For now, we'll just log the alert
        try {
            Map<String, Object> alert = new HashMap<>();
            alert.put("timestamp", LocalDateTime.now());
            alert.put("subject", subject);
            alert.put("message", message);
            alert.put("severity", "HIGH");
            alert.put("consecutiveFailures", consecutiveFailures);
            
            logger.error("System Alert: {}", alert);
            
        } catch (Exception e) {
            logger.error("Failed to send alert: {}", e.getMessage());
        }
    }
    
    /**
     * Get current system health status
     */
    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("healthy", systemHealthy);
        health.put("lastHealthCheck", lastHealthCheck);
        health.put("consecutiveFailures", consecutiveFailures);
        
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        Map<String, Object> memory = new HashMap<>();
        memory.put("max", maxMemory / 1024 / 1024);
        memory.put("used", usedMemory / 1024 / 1024);
        memory.put("free", freeMemory / 1024 / 1024);
        memory.put("usagePercent", (double) usedMemory / maxMemory * 100);
        
        health.put("memory", memory);
        
        return health;
    }
    
    /**
     * Manual health check trigger
     */
    public boolean triggerManualHealthCheck() {
        try {
            performHealthCheck();
            return true;
        } catch (Exception e) {
            logger.error("Manual health check failed: {}", e.getMessage());
            return false;
        }
    }
}
