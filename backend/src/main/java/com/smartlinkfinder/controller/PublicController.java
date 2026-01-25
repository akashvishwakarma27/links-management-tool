package com.smartlinkfinder.controller;

import com.smartlinkfinder.entity.Link;
import com.smartlinkfinder.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "*")
public class PublicController {
    
    @Autowired
    private LinkRepository linkRepository;
    
    @GetMapping("/link/{referenceCode}")
    public ResponseEntity<?> getLinkByReference(@PathVariable String referenceCode) {
        Optional<Link> link = linkRepository.findByReferenceCodeIgnoreCase(referenceCode);
        if (link.isPresent()) {
            return ResponseEntity.ok(link.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sorry, this reference code is not available in our database.");
            error.put("message", "Please verify the code or contact the admin to add this link.");
            return ResponseEntity.status(404).body(error);
        }
    }
}
