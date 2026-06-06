package com.student.fashion_store_management_system.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        // Get images/users/{userId}
        Path uploadPath = Paths.get(uploadDir);

        // Check images/users/{userId} is existing
        // If no, create new images/users/{userId}
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Get data (byte) from multipartFile
        try (InputStream inputStream = multipartFile.getInputStream();) {
            // Merge uploadDir = "images/users/{userId}" and filename = "avatar.jpg" to images/users/{userId}/avatar.jpg
            Path path = uploadPath.resolve(fileName);

            System.out.println("File Path: " + path);
            System.out.println("File Name: " + fileName);

            // Copy data (byte) from multipartFile.getInputStream() to images/users/{userId}/avatar.jpg
            Files.copy(
                    inputStream,
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new IOException("Could not save image file: " + fileName, e);
        }

    }
}
