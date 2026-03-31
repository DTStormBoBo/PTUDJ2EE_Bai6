package main.ptudj2ee_bai6.controller;

import main.ptudj2ee_bai6.model.Cart;
import main.ptudj2ee_bai6.model.Order;
import main.ptudj2ee_bai6.model.Product;
import main.ptudj2ee_bai6.service.OrderService;
import main.ptudj2ee_bai6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    // Get or create cart from session
    private Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // Add product to cart
    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, 
                           @RequestParam(defaultValue = "1") int quantity,
                           HttpSession session) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            Cart cart = getCart(session);
            cart.addItem(product, quantity);
        }
        return "redirect:/cart";
    }

    // View cart page
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Cart cart = getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("totalQuantity", cart.getTotalQuantity());
        model.addAttribute("cartItemCount", cart.getTotalQuantity());
        return "cart/view";
    }

    // Update quantity
    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId,
                                @RequestParam int quantity,
                                HttpSession session) {
        Cart cart = getCart(session);
        cart.updateQuantity(productId, quantity);
        return "redirect:/cart";
    }

    // Remove item from cart
    @GetMapping("/remove/{productId}")
    public String removeItem(@PathVariable Long productId,
                            HttpSession session) {
        Cart cart = getCart(session);
        cart.removeItem(productId);
        return "redirect:/cart";
    }

    // Clear entire cart
    @GetMapping("/clear")
    public String clearCart(HttpSession session) {
        Cart cart = getCart(session);
        cart.clear();
        return "redirect:/cart";
    }

    // Checkout and create order from cart
    @PostMapping("/checkout")
    public String checkout(HttpSession session, Model model, Principal principal) {
        Cart cart = getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        String username = principal != null ? principal.getName() : "guest";
        Order order = orderService.createOrderFromCart(cart, username);
        // Clear cart after order created
        session.removeAttribute("cart");
        model.addAttribute("order", order);
        return "cart/checkout-success";
    }
}