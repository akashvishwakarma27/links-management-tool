package com.smartlinkfinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LinkRequest {
    @NotBlank(message = "Reference code is required")
    @Size(min = 2, max = 20, message = "Reference code must be between 2 and 20 characters")
    private String referenceCode;
    
    @NotBlank(message = "URL is required")
    @Size(max = 2048, message = "URL must not exceed 2048 characters")
    private String fullUrl;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @Size(max = 100, message = "Brand name must not exceed 100 characters")
    private String brandName;
    
    private String status;
    
    public LinkRequest() {}
    
    public String getReferenceCode() { return referenceCode; }
    public void setReferenceCode(String referenceCode) { this.referenceCode = referenceCode; }
    
    public String getFullUrl() { return fullUrl; }
    public void setFullUrl(String fullUrl) { this.fullUrl = fullUrl; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
