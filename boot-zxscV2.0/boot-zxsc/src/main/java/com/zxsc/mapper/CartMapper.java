package com.zxsc.mapper;

import com.zxsc.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CartMapper {
    List<Cart> findByUserId(Long userId);
    
    Cart findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
    
    int insert(Cart cart);
    
    int update(Cart cart);
    
    int deleteById(Long id);
    
    int deleteByUserId(Long userId);
    
    int deleteByIds(@Param("ids") List<Long> ids);
}
