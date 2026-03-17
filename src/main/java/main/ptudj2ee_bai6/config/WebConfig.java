package main.ptudj2ee_bai6.config;

import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Phục vụ tệp từ thư mục upload ngoài project
        String uploadPath = Paths.get("upload").toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(uploadPath);
    }
}
