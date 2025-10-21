package com.example.taskday.controller;

import com.example.taskday.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    
    private final FileService fileService;
    
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String filename = fileService.uploadFile(file);
            return ResponseEntity.ok(filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }
    
    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {
        try {
            byte[] fileContent = fileService.getFile(filename);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        try {
            fileService.deleteFile(filename);
            return ResponseEntity.ok("File deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting file: " + e.getMessage());
        }
    }
}
