package main.ptudj2ee_bai6.repository;

import main.ptudj2ee_bai6.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> { }
