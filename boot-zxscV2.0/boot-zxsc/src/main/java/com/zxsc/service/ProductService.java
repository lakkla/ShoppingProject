package com.zxsc.service;

import com.zxsc.entity.Product;
import com.zxsc.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    public List<Product> findAll() {
        return productMapper.findAll();
    }
    
    public List<Product> findByCategory(Long categoryId) {
        return productMapper.findByCategory(categoryId);
    }
    
    public List<Product> search(String keyword) {
        return productMapper.search(keyword);
    }
    
    public Product findById(Long id) {
        return productMapper.findById(id);
    }
    
    public boolean save(Product product) {
        if (product.getId() == null) {
            return productMapper.insert(product) > 0;
        } else {
            return productMapper.update(product) > 0;
        }
    }
    
    public boolean deleteById(Long id) {
        return productMapper.deleteById(id) > 0;
    }
    
    public boolean updateStock(Long id, Integer quantity) {
        return productMapper.updateStock(id, quantity) > 0;
    }
}
