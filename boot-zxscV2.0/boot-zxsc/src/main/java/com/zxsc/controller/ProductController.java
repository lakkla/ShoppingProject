package com.zxsc.controller;

import com.zxsc.entity.Category;
import com.zxsc.entity.Product;
import com.zxsc.service.CategoryService;
import com.zxsc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/list")
    public String list(@RequestParam(required = false) Long categoryId, 
                      @RequestParam(required = false) String keyword, Model model) {
        List<Product> products;
        if (categoryId != null) {
            products = productService.findByCategory(categoryId);
        } else if (keyword != null && !keyword.isEmpty()) {
            products = productService.search(keyword);
        } else {
            products = productService.findAll();
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategoryId", categoryId);
        model.addAttribute("keyword", keyword);
        return "product/list";
    }
    
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product/detail";
    }
}
