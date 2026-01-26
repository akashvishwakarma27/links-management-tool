package com.smartlinkfinder.service;

import com.smartlinkfinder.entity.User;
import com.smartlinkfinder.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Legacy Data Initialization Service - DISABLED
 * This service is replaced by PersistentDataInitializationService
 * Keeping this file disabled to prevent conflicts
 */
@Service
@Deprecated
public class DataInitializationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        logger.warn("DEPRECATED: DataInitializationService is disabled. Use PersistentDataInitializationService instead.");
        logger.info("Skipping data initialization in deprecated service.");
    }
    
    private void createDefaultUsers() {
        // This method is deprecated - use PersistentDataInitializationService
        logger.warn("DEPRECATED: createDefaultUsers is disabled. Use PersistentDataInitializationService instead.");
    }
}
