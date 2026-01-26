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

@Service
public class DataInitializationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        logger.info("Starting data initialization...");
        
        try {
            createDefaultUsers();
            logger.info("Data initialization completed successfully");
        } catch (Exception e) {
            logger.error("Error during data initialization", e);
        }
    }
    
    private void createDefaultUsers() {
        List<User> defaultUsers = Arrays.asList(
            new User(
                "admin", 
                "admin@smartlinkfinder.com", 
                passwordEncoder.encode("admin123"), 
                User.Role.ADMIN
            ),
            new User(
                "akash_super_admin", 
                "akash.super@admin.com", 
                passwordEncoder.encode("akash123"), 
                User.Role.SUPER_ADMIN
            )
        );
        
        for (User user : defaultUsers) {
            if (!userRepository.existsByUsername(user.getUsername())) {
                userRepository.save(user);
                logger.info("Created default user: {} with role: {}", user.getUsername(), user.getRole());
            } else {
                logger.debug("User {} already exists, skipping creation", user.getUsername());
            }
        }
    }
}
