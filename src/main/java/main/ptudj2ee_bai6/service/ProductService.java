package main.ptudj2ee_bai6.service;

import main.ptudj2ee_bai6.model.*;
import main.ptudj2ee_bai6.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> searchAndFilter(String keyword, Long categoryId, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = "";
        }
        
        // If both keyword and categoryId are provided
        if (categoryId != null && categoryId > 0) {
            return productRepository.findByNameContainingAndCategory_Id(keyword, categoryId, pageable);
        }
        
        // If only keyword is provided
        if (!keyword.isEmpty()) {
            return productRepository.findByNameContaining(keyword, pageable);
        }
        
        // If only categoryId is provided
        if (categoryId != null && categoryId > 0) {
            return productRepository.findByCategory_Id(categoryId, pageable);
        }
        
        // Return all products with pagination
        return productRepository.findAll(pageable);
    }

    public void saveProduct (Product product) {
        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

