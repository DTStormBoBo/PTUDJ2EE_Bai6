package main.ptudj2ee_bai6.repository;

import main.ptudj2ee_bai6.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
