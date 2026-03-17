package main.ptudj2ee_bai6.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tạo ảnh mặc định default.jpg khi ứng dụng khởi động
 * nếu file chưa tồn tại
 */
@Component
public class ImageInitializer implements CommandLineRunner {

    private static final String UPLOAD_DIR = "upload/images";
    private static final String DEFAULT_IMAGE_NAME = "default.jpg";

    @Override
    public void run(String... args) throws Exception {
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath();
        
        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("[ImageInitializer] Thư mục upload được tạo: " + uploadPath);
        }

        // Tạo ảnh mặc định nếu chưa tồn tại
        Path defaultImagePath = uploadPath.resolve(DEFAULT_IMAGE_NAME);
        if (!Files.exists(defaultImagePath)) {
            try {
                createDefaultImage(defaultImagePath.toFile());
                System.out.println("[ImageInitializer] Ảnh mặc định được tạo: " + defaultImagePath);
            } catch (IOException e) {
                System.err.println("[ImageInitializer] Lỗi khi tạo ảnh mặc định: " + e.getMessage());
            }
        } else {
            System.out.println("[ImageInitializer] Ảnh mặc định đã tồn tại: " + defaultImagePath);
        }
    }

    /**
     * Tạo ảnh mặc định 90x70px có văn bản "No Image"
     */
    private void createDefaultImage(File file) throws IOException {
        int width = 90;
        int height = 70;
        
        // Tạo BufferedImage với nền xám
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Vẽ nền
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRect(0, 0, width, height);

        // Vẽ viền
        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(0, 0, width - 1, height - 1);

        // Vẽ biểu tượng ảnh (icon)
        g2d.setColor(new Color(100, 100, 100));
        // Vẽ một hình vuông nhỏ (đại diện cho ảnh)
        g2d.fillRect(15, 15, 30, 25);
        // Vẽ 2 hình tròn (đại diện cho ảnh)
        g2d.setColor(new Color(150, 150, 150));
        g2d.fillOval(20, 10, 12, 12);
        g2d.fillOval(38, 18, 10, 10);

        // Vẽ đường kẻ (đại diện cho landscape)
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawLine(15, 40, 60, 40);

        // Vẽ text "No Image"
        g2d.setColor(new Color(80, 80, 80));
        Font font = new Font("Arial", Font.BOLD, 10);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);
        String text = "No Image";
        int x = (width - metrics.stringWidth(text)) / 2;
        int y = 55;
        g2d.drawString(text, x, y);

        g2d.dispose();

        // Lưu ảnh dưới dạng JPEG
        ImageIO.write(image, "jpg", file);
    }
}
