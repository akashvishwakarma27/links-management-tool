package com.smartlinkfinder.service;

import com.smartlinkfinder.entity.Link;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelUploadService {

    @Autowired
    private LinkService linkService;

    public List<Link> uploadExcelFile(MultipartFile file) throws IOException {
        List<Link> links = new ArrayList<>();
        
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            
            Iterator<Row> rowIterator = sheet.iterator();
            
            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            
            int rowNum = 0;
            int successCount = 0;
            int errorCount = 0;
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowNum++;
                
                try {
                    Link link = createLinkFromRow(row);
                    if (link != null) {
                        links.add(link);
                        successCount++;
                    }
                } catch (Exception e) {
                    System.err.println("Error processing row " + rowNum + ": " + e.getMessage());
                    errorCount++;
                }
            }
            
            workbook.close();
            
            System.out.println("Excel upload completed. Success: " + successCount + ", Errors: " + errorCount);
        }
        
        return links;
    }
    
    private Link createLinkFromRow(Row row) {
        // Reference Code (Column A)
        Cell referenceCodeCell = row.getCell(0);
        if (referenceCodeCell == null || referenceCodeCell.getCellType() == CellType.BLANK) {
            throw new IllegalArgumentException("Reference Code is required");
        }
        
        String referenceCode = getCellValue(referenceCodeCell);
        if (referenceCode == null || referenceCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Reference Code cannot be empty");
        }
        
        // Full URL (Column B)
        Cell fullUrlCell = row.getCell(1);
        if (fullUrlCell == null || fullUrlCell.getCellType() == CellType.BLANK) {
            throw new IllegalArgumentException("Full URL is required");
        }
        
        String fullUrl = getCellValue(fullUrlCell);
        if (fullUrl == null || fullUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Full URL cannot be empty");
        }
        
        // Validate URL format
        if (!fullUrl.startsWith("http://") && !fullUrl.startsWith("https://")) {
            throw new IllegalArgumentException("Full URL must start with http:// or https://");
        }
        
        // Create Link object
        Link link = new Link();
        link.setReferenceCode(referenceCode.trim());
        link.setFullUrl(fullUrl.trim());
        
        // Set default status to ACTIVE
        link.setStatus("ACTIVE");
        
        // Note: Brand Name and Description will be extracted from reference code
        // in the frontend display, not stored in database
        
        return link;
    }
    
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // For formula cells, try to get the cached value
                switch (cell.getCachedFormulaResultType()) {
                    case STRING:
                        return cell.getStringCellValue().trim();
                    case NUMERIC:
                        return String.valueOf((long) cell.getNumericCellValue());
                    case BOOLEAN:
                        return String.valueOf(cell.getBooleanCellValue());
                    default:
                        return null;
                }
            default:
                return null;
        }
    }
    
    public List<Link> processAndSaveLinks(List<Link> links) {
        List<Link> savedLinks = new ArrayList<>();
        
        for (Link link : links) {
            try {
                // Check if reference code already exists
                if (linkService.existsByReferenceCode(link.getReferenceCode())) {
                    System.out.println("Skipping duplicate reference code: " + link.getReferenceCode());
                    continue;
                }
                
                Link savedLink = linkService.createLink(link);
                savedLinks.add(savedLink);
                System.out.println("Saved link: " + link.getReferenceCode());
            } catch (Exception e) {
                System.err.println("Error saving link " + link.getReferenceCode() + ": " + e.getMessage());
            }
        }
        
        return savedLinks;
    }
}
