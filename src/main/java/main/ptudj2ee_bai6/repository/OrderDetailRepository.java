package main.ptudj2ee_bai6.repository;

import main.ptudj2ee_bai6.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
