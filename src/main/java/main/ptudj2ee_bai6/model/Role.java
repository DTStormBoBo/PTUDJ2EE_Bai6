package main.ptudj2ee_bai6.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Vai tro khong duoc de trong")
    @Size(max = 100, message = "Ten vai tro khong duoc vuot qua 100 ky tu")
    @Pattern(regexp = "^ROLE_[A-Z_]+$", message = "Vai tro phai co dinh dang ROLE_X")
    @Column(nullable = false, length = 100)
    private String name;
}