package com.smartlinkfinder.controller;

import com.smartlinkfinder.dto.LinkRequest;
import com.smartlinkfinder.entity.Link;
import com.smartlinkfinder.service.LinkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/links")
@CrossOrigin(origins = "http://localhost:3000")
public class LinkController {
    
    @Autowired
    private LinkService linkService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Page<Link>> getAllLinks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Link> links = linkService.getAllLinks(pageable);
        return ResponseEntity.ok(links);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getLinkById(@PathVariable Long id) {
        Optional<Link> link = linkService.getLinkById(id);
        if (link.isPresent()) {
            return ResponseEntity.ok(link.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Link not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping("/reference/{referenceCode}")
    public ResponseEntity<?> getLinkByReferenceCode(@PathVariable String referenceCode) {
        Optional<Link> link = linkService.getLinkByReferenceCode(referenceCode);
        if (link.isPresent()) {
            return ResponseEntity.ok(link.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sorry, this reference code is not available in our database.");
            error.put("message", "Please verify the code or contact the admin to add this link.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchLinks(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        // Limit size for better performance
        if (size > 50) {
            size = 50;
        }
        
        // Limit page for performance
        if (page > 10) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Page limit exceeded for performance");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("referenceCode").ascending());
        Page<Link> links = linkService.searchLinks(q, pageable);
        
        return ResponseEntity.ok(links);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> createLink(@Valid @RequestBody LinkRequest linkRequest, Authentication authentication) {
        try {
            Link link = linkService.createLink(linkRequest, authentication.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(link);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLink(@PathVariable Long id, @Valid @RequestBody LinkRequest linkRequest) {
        try {
            Link link = linkService.updateLink(id, linkRequest);
            return ResponseEntity.ok(link);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteLink(@PathVariable Long id) {
        try {
            linkService.deleteLink(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Link deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
