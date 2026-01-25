package com.smartlinkfinder.repository;

import com.smartlinkfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // Admin management methods
    List<User> findByRole(User.Role role);
    List<User> findByRoleOrderByCreatedAtDesc(User.Role role);
}
