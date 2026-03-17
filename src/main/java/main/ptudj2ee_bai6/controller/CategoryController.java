package main.ptudj2ee_bai6.controller;

import jakarta.validation.Valid;
import main.ptudj2ee_bai6.model.Category;
import main.ptudj2ee_bai6.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public String index(Model model) {
		model.addAttribute("listcategory", categoryService.getAllCategories());
		return "categories/list";
	}

	@GetMapping("/categories/add")
	public String add(Model model) {
		model.addAttribute("category", new Category());
		return "categories/add";
	}

	@PostMapping("/categories/save")
	public String save(@Valid Category newCategory, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("category", newCategory);
			return "categories/add";
		}

		categoryService.saveCategory(newCategory);
		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Category find = categoryService.getCategoryById(id);
		if (find == null) {
			return "redirect:/categories";
		}

		model.addAttribute("category", find);
		return "categories/edit";
	}

	@PostMapping("/categories/edit")
	public String edit(@Valid Category editCategory, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("category", editCategory);
			return "categories/edit";
		}

		categoryService.saveCategory(editCategory);
		return "redirect:/categories";
	}

	@PostMapping("/categories/delete/{id}")
	public String delete(@PathVariable Integer id) {
		categoryService.deleteCategory(id);
		return "redirect:/categories";
	}
}
