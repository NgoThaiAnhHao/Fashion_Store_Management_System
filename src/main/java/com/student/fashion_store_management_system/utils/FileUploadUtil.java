package com.student.fashion_store_management_system.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Utility class dùng để lưu file upload từ người dùng vào thư mục local của project.
 *
 * Ví dụ:
 * - Upload avatar user: uploads/users/{userId}/avatar.png
 * - Upload ảnh product: uploads/products/{productId}/shirt.png
 * - Upload logo custom áo: uploads/custom-logos/logo.png
 */
public class FileUploadUtil {

    /**
     * Lưu file upload vào thư mục chỉ định.
     *
     * @param uploadDir     thư mục muốn lưu file, ví dụ: "uploads/custom-logos"
     * @param fileName      tên file sau khi xử lý, ví dụ: "abc-uuid.png"
     * @param multipartFile file được upload từ form HTML
     * @throws IOException nếu không tạo được thư mục hoặc không lưu được file
     */
    public static void saveFile(String uploadDir,
                                String fileName,
                                MultipartFile multipartFile) throws IOException {

        // Chuyển đường dẫn dạng String thành Path để Java có thể thao tác với file/folder
        Path uploadPath = Paths.get(uploadDir);

        // Nếu thư mục upload chưa tồn tại thì tạo mới
        // Ví dụ chưa có "uploads/custom-logos" thì sẽ tự động tạo folder này
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Lấy dữ liệu file upload dưới dạng InputStream
        try (InputStream inputStream = multipartFile.getInputStream()) {

            // Ghép thư mục upload với tên file
            // Ví dụ: uploads/custom-logos + abc.png = uploads/custom-logos/abc.png
            Path path = uploadPath.resolve(fileName);

            // In ra console để debug đường dẫn file khi upload
            System.out.println("File Path: " + path);
            System.out.println("File Name: " + fileName);

            // Copy dữ liệu từ file upload vào đường dẫn cần lưu
            // REPLACE_EXISTING nghĩa là nếu file trùng tên thì ghi đè
            Files.copy(
                    inputStream,
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {
            // Nếu có lỗi khi lưu file thì ném lỗi rõ ràng hơn
            throw new IOException("Could not save image file: " + fileName, e);
        }
    }
}