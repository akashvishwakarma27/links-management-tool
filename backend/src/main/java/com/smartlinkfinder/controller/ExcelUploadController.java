package com.smartlinkfinder.controller;

import com.smartlinkfinder.entity.Link;
import com.smartlinkfinder.service.ExcelUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/excel")
@CrossOrigin(origins = "*")
public class ExcelUploadController {

    @Autowired
    private ExcelUploadService excelUploadService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate file
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "Please select a file to upload");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") && 
                !contentType.equals("application/vnd.ms-excel"))) {
                response.put("success", false);
                response.put("message", "Please upload a valid Excel file (.xlsx or .xls)");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Process Excel file
            List<Link> links = excelUploadService.uploadExcelFile(file);
            
            if (links.isEmpty()) {
                response.put("success", false);
                response.put("message", "No valid data found in Excel file");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Save links to database
            List<Link> savedLinks = excelUploadService.processAndSaveLinks(links);
            
            response.put("success", true);
            response.put("message", "Excel file processed successfully");
            response.put("totalRows", links.size());
            response.put("savedRows", savedLinks.size());
            response.put("skippedRows", links.size() - savedLinks.size());
            response.put("data", savedLinks);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error processing Excel file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/template")
    public ResponseEntity<Map<String, Object>> downloadTemplate() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            response.put("success", true);
            response.put("message", "Excel template downloaded successfully");
            response.put("templateUrl", "/api/excel/download-template");
            response.put("columns", new String[]{
                "Reference Code (Required)",
                "Full URL (Required)"
            });
            response.put("sampleData", new Object[][]{
                {"PI-31001", "https://example.com/doc1.pdf"},
                {"PI-31002", "https://example.com/doc2.pdf"},
                {"PI-32001", "https://example.com/doc3.pdf"},
                {"PI-33001", "https://example.com/doc4.pdf"},
                {"PI-34001", "https://example.com/doc5.pdf"},
                {"PI-35001", "https://example.com/doc6.pdf"}
            });
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error generating template: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
