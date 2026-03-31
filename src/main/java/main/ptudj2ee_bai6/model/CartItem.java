package main.ptudj2ee_bai6.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Product product;
    private int quantity;

    // Constructors
    public CartItem() {}

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters & Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Calculate total price for this item
    public long getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
