package main.ptudj2ee_bai6.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {
    private static final String UPLOAD_DIR = "upload/images";

    public FileUploadService() {
        try {
            // Lấy đường dẫn tuyệt đối từ working directory
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Thư mục upload được tạo: " + uploadPath);
            }
        } catch (IOException e) {
            System.err.println("Không thể tạo thư mục upload: " + e.getMessage());
        }
    }

    public String saveProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // Tạo tên file duy nhất
            String originalName = file.getOriginalFilename();
            String fileExtension = originalName.substring(originalName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Lưu file vào thư mục tuyệt đối
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath();
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());

            System.out.println("Ảnh được lưu tại: " + filePath);
            // Trả về đường dẫn tương đối để lưu vào database
            return "/upload/images/" + uniqueFileName;
        } catch (IOException e) {
            System.err.println("Lỗi khi tải ảnh: " + e.getMessage());
            return null;
        }
    }

    public void deleteProductImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }

        try {
            // Xóa file từ đường dẫn tuyệt đối
            String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            Path filePath = Paths.get(UPLOAD_DIR).toAbsolutePath().resolve(fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Ảnh được xóa: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi xóa ảnh: " + e.getMessage());
        }
    }
}
