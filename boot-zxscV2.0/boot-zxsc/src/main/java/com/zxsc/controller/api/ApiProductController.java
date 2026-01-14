package com.zxsc.controller.api;

import com.zxsc.common.Result;
import com.zxsc.entity.Category;
import com.zxsc.entity.Product;
import com.zxsc.service.CategoryService;
import com.zxsc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ApiProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/list")
    public Result<List<Product>> list(@RequestParam(required = false) Long categoryId, 
                                      @RequestParam(required = false) String keyword) {
        List<Product> products;
        if (categoryId != null) {
            products = productService.findByCategory(categoryId);
        } else if (keyword != null && !keyword.isEmpty()) {
            products = productService.search(keyword);
        } else {
            products = productService.findAll();
        }
        return Result.success("查询成功", products);
    }
    
    @GetMapping("/detail/{id}")
    public Result<Product> detail(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product != null) {
            return Result.success("查询成功", product);
        }
        return Result.error("商品不存在");
    }
    
    @GetMapping("/categories")
    public Result<List<Category>> categories() {
        List<Category> categories = categoryService.findAll();
        return Result.success("查询成功", categories);
    }
}
