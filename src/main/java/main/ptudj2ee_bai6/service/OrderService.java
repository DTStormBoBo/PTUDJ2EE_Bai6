package main.ptudj2ee_bai6.service;

import main.ptudj2ee_bai6.model.*;
import main.ptudj2ee_bai6.repository.OrderDetailRepository;
import main.ptudj2ee_bai6.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public Order createOrderFromCart(Cart cart, String username) {
        if (cart == null || cart.isEmpty()) {
            return null;
        }
        Order order = new Order();
        order.setUsername(username);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(cart.getTotalPrice());

        // Save order first to generate ID
        order = orderRepository.save(order);

        for (CartItem item : cart.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(item.getProduct().getPrice());
            detail.setLineTotal(item.getTotalPrice());
            orderDetailRepository.save(detail);
            order.getDetails().add(detail);
        }
        return order;
    }
}
