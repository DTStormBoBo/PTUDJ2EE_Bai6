package main.ptudj2ee_bai6.controller;

import jakarta.validation.Valid;
import main.ptudj2ee_bai6.model.*;
import main.ptudj2ee_bai6.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/")
    public String Root() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String Index(Model model, HttpServletRequest request) {
        model.addAttribute("listproduct", productService.getAllProducts());
        model.addAttribute("showNavbar", true);
        model.addAttribute("currentUser", request.getRemoteUser());
        model.addAttribute("pageTitle", "Danh Sách Sản Phẩm");
        model.addAttribute("pageSubtitle", "Quản lý toàn bộ sản phẩm của bạn");
        model.addAttribute("headerButtonsHtml", "<a class='btn btn-add' href='/products/add'>Thêm Sản Phẩm</a>");
        return "products/list";
    }

    @GetMapping("/products/add")
    public String Add(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/add";
    }

    @GetMapping("/product/add")
    public String AddAlias(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/add";
    }

    @PostMapping("/products/save")
    public String Save(@Valid Product newProduct, BindingResult result,
                       @RequestParam(value = "categoryId", required = false) Integer categoryId,
                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile, 
                       Model model) {
        
        // Xử lý category
        if (categoryId != null && categoryId > 0) {
            Category category = categoryService.getCategoryById(categoryId);
            if (category != null) {
                newProduct.setCategory(category);
            } else {
                result.rejectValue("category", "error.category", "Danh mục không tồn tại");
            }
        } else {
            result.rejectValue("category", "error.category", "Vui lòng chọn danh mục");
        }
        
        // Validate
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "products/add";
        }
        
        // Xử lý ảnh upload từ file
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imagePath = fileUploadService.saveProductImage(imageFile);
                if (imagePath != null) {
                    newProduct.setImage(imagePath);
                    System.out.println("Upload ảnh thành công: " + imagePath);
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi tải ảnh: " + e.getMessage());
                model.addAttribute("product", newProduct);
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("errorMessage", "Lỗi khi tải ảnh: " + e.getMessage());
                return "products/add";
            }
        } else {
            // Sử dụng ảnh mặc định nếu không upload
            newProduct.setImage("/upload/images/default.jpg");
        }
        
        // Lưu vào database
        try {
            productService.saveProduct(newProduct);
            System.out.println("Lưu sản phẩm thành công: " + newProduct.getName());
        } catch (Exception e) {
            System.out.println("Lỗi khi lưu sản phẩm: " + e.getMessage());
            model.addAttribute("product", newProduct);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("errorMessage", "Lỗi khi lưu sản phẩm: " + e.getMessage());
            return "products/add";
        }
        return "redirect:/products";
    }

    @PostMapping("/product/add")
    public String SaveAlias(@Valid Product newProduct, BindingResult result,
                            @RequestParam(value = "categoryId", required = false) Integer categoryId,
                            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                            Model model) {
        return Save(newProduct, result, categoryId, imageFile, model);
    }

    @GetMapping("/products/edit/{id}")
    public String Edit(@PathVariable Long id, Model model) {
        Product find = productService.getProductById(id);
        if (find == null) {
            return "redirect:/products"; // Product not found, redirect to list
        }
        model.addAttribute("product", find);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/edit";
    }

    @GetMapping("/products/edit")
    public String EditFallback() {
        // Fallback: if user access /products/edit without ID, redirect to products list
        return "redirect:/products";
    }

    @PostMapping("/products/edit")
    public String Edit(@Valid Product editProduct,
                       BindingResult result,
                       @RequestParam(value = "categoryId", required = false) Integer categoryId,
                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                       Model model) {
        try {
            System.out.println("=== EDIT POST ===");
            System.out.println("Product ID: " + editProduct.getId());
            System.out.println("Product Name: " + editProduct.getName());
            System.out.println("Category ID: " + categoryId);

            // Validate product ID exists
            if (editProduct.getId() == null || editProduct.getId() <= 0) {
                System.out.println("Invalid product ID");
                return "redirect:/products";
            }

            // Xử lý category
            if (categoryId != null && categoryId > 0) {
                Category category = categoryService.getCategoryById(categoryId);
                if (category != null) {
                    editProduct.setCategory(category);
                } else {
                    result.rejectValue("category", "error.category", "Danh mục không tồn tại");
                }
            } else {
                result.rejectValue("category", "error.category", "Vui lòng chọn danh mục");
            }

            // If validation failed, show form with errors
            if (result.hasErrors()) {
                System.out.println("VALIDATION ERRORS:");
                result.getAllErrors().forEach(e -> System.out.println("  - " + e.getDefaultMessage()));
                model.addAttribute("product", editProduct);
                model.addAttribute("categories", categoryService.getAllCategories());
                return "products/edit";
            }

            // Process image upload if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String imagePath = fileUploadService.saveProductImage(imageFile);
                    if (imagePath != null) {
                        // Xóa ảnh cũ nếu tồn tại
                        if (editProduct.getImage() != null && !editProduct.getImage().isEmpty()) {
                            fileUploadService.deleteProductImage(editProduct.getImage());
                        }
                        editProduct.setImage(imagePath);
                        System.out.println("Cập nhật ảnh thành công: " + imagePath);
                    }
                } catch (Exception imgEx) {
                    System.out.println("Image upload error: " + imgEx.getMessage());
                    model.addAttribute("product", editProduct);
                    model.addAttribute("categories", categoryService.getAllCategories());
                    model.addAttribute("errorMessage", "Lỗi khi tải ảnh: " + imgEx.getMessage());
                    return "products/edit";
                }
            }

            System.out.println("Updating product with ID: " + editProduct.getId());
            productService.saveProduct(editProduct);
            System.out.println("Update successful, redirecting to /products");
            return "redirect:/products";

        } catch (Exception e) {
            System.out.println("FATAL ERROR in edit POST: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("product", editProduct);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            return "products/edit";
        }
    }

    @GetMapping("/products/delete/{id}")
    public String Delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}