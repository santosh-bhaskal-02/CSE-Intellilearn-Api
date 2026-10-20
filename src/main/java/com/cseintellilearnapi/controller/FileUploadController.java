package com.cseintellilearnapi.controller;

import com.cseintellilearnapi.exception.BadRequestException;
import com.cseintellilearnapi.exception.ResourceNotFoundException;
import com.cseintellilearnapi.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file cannot be empty");
        }
        try {
            FileStorageService.StoredFileInfo fileInfo = fileStorageService.storeFile(file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "fileUrl", fileInfo.downloadUri(),
                    "fileName", fileInfo.originalFileName()
            ));
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file: " + ex.getMessage(), ex);
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        if (resource == null) {
            throw new ResourceNotFoundException("File", "name", fileName);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
