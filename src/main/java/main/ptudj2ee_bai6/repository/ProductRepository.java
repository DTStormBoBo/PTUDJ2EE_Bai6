package main.ptudj2ee_bai6.repository;

import main.ptudj2ee_bai6.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
