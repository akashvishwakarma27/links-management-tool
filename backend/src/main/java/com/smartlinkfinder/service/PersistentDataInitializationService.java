package com.smartlinkfinder.service;

import com.smartlinkfinder.entity.Link;
import com.smartlinkfinder.entity.User;
import com.smartlinkfinder.repository.LinkRepository;
import com.smartlinkfinder.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Persistent Data Initialization Service
 * Ensures data survives across deployments and database changes
 */
@Service
@Transactional
public class PersistentDataInitializationService {
    
    private static final Logger logger = LoggerFactory.getLogger(PersistentDataInitializationService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LinkRepository linkRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @EventListener(ApplicationReadyEvent.class)
    public void initializePersistentData() {
        logger.info("Starting persistent data initialization...");
        
        try {
            initializeUsers();
            initializeSampleLinks();
            logger.info("Persistent data initialization completed successfully");
        } catch (Exception e) {
            logger.error("Error during persistent data initialization", e);
        }
    }
    
    /**
     * Initialize users with persistence protection
     */
    private void initializeUsers() {
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
            try {
                // Check if user already exists
                if (!userRepository.existsByUsername(user.getUsername())) {
                    userRepository.save(user);
                    logger.info("Created persistent user: {} with role: {}", user.getUsername(), user.getRole());
                } else {
                    logger.debug("User {} already exists, skipping creation", user.getUsername());
                    
                    // Update password if needed (for password changes)
                    User existingUser = userRepository.findByUsername(user.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found: " + user.getUsername()));
                    
                    // Only update if password is different
                    if (!passwordEncoder.matches("admin123", existingUser.getPassword()) && 
                        user.getUsername().equals("admin")) {
                        existingUser.setPassword(passwordEncoder.encode("admin123"));
                        userRepository.save(existingUser);
                        logger.info("Updated password for user: {}", user.getUsername());
                    }
                    
                    if (!passwordEncoder.matches("akash123", existingUser.getPassword()) && 
                        user.getUsername().equals("akash_super_admin")) {
                        existingUser.setPassword(passwordEncoder.encode("akash123"));
                        userRepository.save(existingUser);
                        logger.info("Updated password for user: {}", user.getUsername());
                    }
                }
            } catch (DataIntegrityViolationException e) {
                logger.warn("User {} already exists with different data: {}", user.getUsername(), e.getMessage());
            } catch (Exception e) {
                logger.error("Error creating user {}: {}", user.getUsername(), e.getMessage());
            }
        }
    }
    
    /**
     * Initialize sample links with persistence protection
     */
    private void initializeSampleLinks() {
        List<Link> sampleLinks = Arrays.asList(
            new Link(
                "PI-31001", 
                "https://assets.company.com/pharma/egypt/singrix/pi_31001_v1.pdf", 
                "Singrix Product Information - Egypt", 
                "Singrix", 
                "ACTIVE"
            ),
            new Link(
                "PI-32001", 
                "https://assets.company.com/pharma/india/singrix/pi_32001_v1.pdf", 
                "Singrix Product Information - India", 
                "Singrix", 
                "ACTIVE"
            ),
            new Link(
                "PI-33001", 
                "https://assets.company.com/pharma/pakistan/singrix/pi_33001_v1.pdf", 
                "Singrix Product Information - Pakistan", 
                "Singrix", 
                "ACTIVE"
            ),
            new Link(
                "PI-34001", 
                "https://assets.company.com/pharma/gulf/ventolin/pi_34001_v1.pdf", 
                "Ventolin Product Information - Gulf", 
                "Ventolin", 
                "ACTIVE"
            ),
            new Link(
                "PI-35001", 
                "https://assets.company.com/pharma/saudi/panadol/pi_35001_v1.pdf", 
                "Panadol Product Information - Saudi", 
                "Panadol", 
                "ACTIVE"
            )
        );
        
        for (Link link : sampleLinks) {
            try {
                // Check if link already exists
                if (!linkRepository.existsByReferenceCode(link.getReferenceCode())) {
                    link.setCreatedAt(LocalDateTime.now());
                    link.setUpdatedAt(LocalDateTime.now());
                    linkRepository.save(link);
                    logger.info("Created persistent link: {} - {}", link.getReferenceCode(), link.getDescription());
                } else {
                    logger.debug("Link {} already exists, skipping creation", link.getReferenceCode());
                }
            } catch (DataIntegrityViolationException e) {
                logger.warn("Link {} already exists with different data: {}", link.getReferenceCode(), e.getMessage());
            } catch (Exception e) {
                logger.error("Error creating link {}: {}", link.getReferenceCode(), e.getMessage());
            }
        }
    }
    
    /**
     * Get data statistics for monitoring
     */
    public DataStatistics getDataStatistics() {
        try {
            long userCount = userRepository.count();
            long linkCount = linkRepository.count();
            
            return new DataStatistics(userCount, linkCount);
        } catch (Exception e) {
            logger.error("Error getting data statistics", e);
            return new DataStatistics(0, 0);
        }
    }
    
    /**
     * Data statistics holder class
     */
    public static class DataStatistics {
        private final long userCount;
        private final long linkCount;
        
        public DataStatistics(long userCount, long linkCount) {
            this.userCount = userCount;
            this.linkCount = linkCount;
        }
        
        public long getUserCount() { return userCount; }
        public long getLinkCount() { return linkCount; }
        
        @Override
        public String toString() {
            return String.format("DataStatistics{users=%d, links=%d}", userCount, linkCount);
        }
    }
}
