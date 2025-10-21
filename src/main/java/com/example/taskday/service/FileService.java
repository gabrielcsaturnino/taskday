package com.example.taskday.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    
    private static final String UPLOAD_DIR = "uploads/";
    
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        // Criar diretório se não existir
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Gerar nome único para o arquivo
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        
        // Salvar arquivo
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);
        
        return uniqueFilename;
    }
    
    public void deleteFile(String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR + filename);
        Files.deleteIfExists(filePath);
    }
    
    public byte[] getFile(String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR + filename);
        return Files.readAllBytes(filePath);
    }
}
