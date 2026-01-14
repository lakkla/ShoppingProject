package com.zxsc.mapper;

import com.zxsc.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> findAll();
    
    List<Product> findByCategory(Long categoryId);
    
    List<Product> search(String keyword);
    
    Product findById(Long id);
    
    int insert(Product product);
    
    int update(Product product);
    
    int deleteById(Long id);
    
    int updateStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}
