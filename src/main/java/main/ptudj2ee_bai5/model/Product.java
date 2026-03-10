package main.ptudj2ee_bai5.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Column(nullable = false, length = 255)
    private String name;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Min(value = 1, message = "Giá sản phẩm không được nhỏ hơn 1")
    @Max(value = 9999999, message = "Giá sản phẩm không được lơn hơn 9999999")
    @Column(nullable = false) // Cột không được null
    private long price;

    @Length(min = 0, max = 200, message = "Tên hình ảnh không quá 200 kí tự")
    @Column(length = 200) // Độ dài tối đa 200 ký tự
    private String image;

    @ManyToOne // Quan hệ nhiều sản phẩm thuộc 1 danh mục
    @JoinColumn(name = "category_id", nullable = false) // Tên cột liên kết trong bảng product
    private Category category;
    
    @Transient
    private MultipartFile imageFile;
}