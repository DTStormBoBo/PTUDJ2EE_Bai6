package main.ptudj2ee_bai6.repository;

import main.ptudj2ee_bai6.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Search by name containing keyword with pagination
    Page<Product> findByNameContaining(String name, Pageable pageable);
    
    // Search by name and category with pagination
    Page<Product> findByNameContainingAndCategory_Id(String name, Long categoryId, Pageable pageable);
    
    // Filter by category only with pagination
    Page<Product> findByCategory_Id(Long categoryId, Pageable pageable);
}
