package com.smartlinkfinder.repository;

import com.smartlinkfinder.entity.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByReferenceCodeIgnoreCase(String referenceCode);
    
    @Query("SELECT l FROM Link l WHERE " +
           "LOWER(l.referenceCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.brandName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.fullUrl) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Link> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    boolean existsByReferenceCodeIgnoreCase(String referenceCode);
}
