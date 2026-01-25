package com.smartlinkfinder.service;

import com.smartlinkfinder.dto.LinkRequest;
import com.smartlinkfinder.entity.Link;
import com.smartlinkfinder.entity.User;
import com.smartlinkfinder.repository.LinkRepository;
import com.smartlinkfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LinkService {
    
    @Autowired
    private LinkRepository linkRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Link createLink(LinkRequest linkRequest, String username) {
        if (linkRepository.existsByReferenceCodeIgnoreCase(linkRequest.getReferenceCode())) {
            throw new RuntimeException("Reference code already exists");
        }
        
        User addedBy = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Link link = new Link();
        link.setReferenceCode(linkRequest.getReferenceCode());
        link.setFullUrl(linkRequest.getFullUrl());
        link.setDescription(linkRequest.getDescription());
        link.setBrandName(linkRequest.getBrandName());
        
        link.setAddedBy(addedBy);
        
        return linkRepository.save(link);
    }
    
    public Link updateLink(Long id, LinkRequest linkRequest) {
        Link existingLink = linkRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Link not found"));
        
        if (!existingLink.getReferenceCode().equalsIgnoreCase(linkRequest.getReferenceCode()) &&
            linkRepository.existsByReferenceCodeIgnoreCase(linkRequest.getReferenceCode())) {
            throw new RuntimeException("Reference code already exists");
        }
        
        existingLink.setReferenceCode(linkRequest.getReferenceCode());
        existingLink.setFullUrl(linkRequest.getFullUrl());
        existingLink.setDescription(linkRequest.getDescription());
        existingLink.setBrandName(linkRequest.getBrandName());
        
        return linkRepository.save(existingLink);
    }
    
    public void deleteLink(Long id) {
        if (!linkRepository.existsById(id)) {
            throw new RuntimeException("Link not found");
        }
        linkRepository.deleteById(id);
    }
    
    public Optional<Link> getLinkById(Long id) {
        return linkRepository.findById(id);
    }
    
    public Optional<Link> getLinkByReferenceCode(String referenceCode) {
        return linkRepository.findByReferenceCodeIgnoreCase(referenceCode);
    }
    
    public Page<Link> getAllLinks(Pageable pageable) {
        return linkRepository.findAll(pageable);
    }
    
    public Page<Link> searchLinks(String searchTerm, Pageable pageable) {
        return linkRepository.findBySearchTerm(searchTerm, pageable);
    }
    
    public boolean existsByReferenceCode(String referenceCode) {
        return linkRepository.existsByReferenceCodeIgnoreCase(referenceCode);
    }
    
    public Link createLink(Link link) {
        if (linkRepository.existsByReferenceCodeIgnoreCase(link.getReferenceCode())) {
            throw new RuntimeException("Reference code already exists: " + link.getReferenceCode());
        }
        
        return linkRepository.save(link);
    }
}
