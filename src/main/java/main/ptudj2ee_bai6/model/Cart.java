package main.ptudj2ee_bai6.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<CartItem> items;

    // Constructor
    public Cart() {
        this.items = new ArrayList<>();
    }

    // Getters & Setters
    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    // Add item to cart
    public void addItem(Product product, int quantity) {
        // Check if product already exists in cart
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // If not found, add new CartItem
        items.add(new CartItem(product, quantity));
    }

    // Remove item from cart by product ID
    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    // Update quantity for a product
    public void updateQuantity(Long productId, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                if (quantity <= 0) {
                    removeItem(productId);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    // Get total price of entire cart
    public long getTotalPrice() {
        long total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Get number of items in cart
    public int getItemCount() {
        return items.size();
    }

    // Get total quantity (sum of all quantities)
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    // Check if cart is empty
    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Clear entire cart
    public void clear() {
        items.clear();
    }
}
