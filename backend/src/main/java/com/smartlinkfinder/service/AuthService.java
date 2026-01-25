package com.smartlinkfinder.service;

import com.smartlinkfinder.dto.AuthRequest;
import com.smartlinkfinder.dto.AuthResponse;
import com.smartlinkfinder.entity.User;
import com.smartlinkfinder.repository.UserRepository;
import com.smartlinkfinder.security.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public AuthResponse authenticate(AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verify password
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        // Generate real JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
    
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    // Admin management methods
    public List<User> getAllAdmins() {
        return userRepository.findByRoleOrderByCreatedAtDesc(User.Role.ADMIN);
    }
    
    public void deleteAdmin(Long adminId) {
        User admin = userRepository.findById(adminId)
            .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        // Prevent deletion of SUPER_ADMIN users
        if (admin.getRole() == User.Role.SUPER_ADMIN) {
            throw new RuntimeException("Cannot delete SUPER_ADMIN user");
        }
        
        userRepository.delete(admin);
    }
    
    public User getAdminById(Long adminId) {
        return userRepository.findById(adminId)
            .orElseThrow(() -> new RuntimeException("Admin not found"));
    }
    
    @PostConstruct
    public void createTestUsers() {
        try {
            // Create admin user if not exists
            if (!userRepository.existsByUsername("admin")) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@smartlinkfinder.com");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setRole(User.Role.ADMIN);
                userRepository.save(adminUser);
                System.out.println("Created admin user: admin/admin123");
            }
            
            // Create super admin user if not exists
            if (!userRepository.existsByUsername("akash_super_admin")) {
                User superAdminUser = new User();
                superAdminUser.setUsername("akash_super_admin");
                superAdminUser.setEmail("akash.super@admin.com");
                superAdminUser.setPassword(passwordEncoder.encode("akash123"));
                superAdminUser.setRole(User.Role.SUPER_ADMIN);
                userRepository.save(superAdminUser);
                System.out.println("Created super admin user: akash_super_admin/akash123");
            }
        } catch (Exception e) {
            System.out.println("Test users creation failed or already exists: " + e.getMessage());
        }
    }
}
