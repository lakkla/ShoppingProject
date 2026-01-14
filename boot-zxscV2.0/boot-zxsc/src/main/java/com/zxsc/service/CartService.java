package com.zxsc.service;

import com.zxsc.entity.Cart;
import com.zxsc.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CartService {
    
    @Autowired
    private CartMapper cartMapper;
    
    public List<Cart> findByUserId(Long userId) {
        return cartMapper.findByUserId(userId);
    }
    
    @Transactional
    public boolean addToCart(Long userId, Long productId, Integer quantity) {
        Cart cart = cartMapper.findByUserAndProduct(userId, productId);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + quantity);
            return cartMapper.update(cart) > 0;
        } else {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            return cartMapper.insert(cart) > 0;
        }
    }
    
    public boolean updateQuantity(Long id, Integer quantity) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setQuantity(quantity);
        return cartMapper.update(cart) > 0;
    }
    
    public boolean deleteById(Long id) {
        return cartMapper.deleteById(id) > 0;
    }
    
    public boolean clearCart(Long userId) {
        return cartMapper.deleteByUserId(userId) > 0;
    }
    
    public boolean deleteByIds(List<Long> ids) {
        return cartMapper.deleteByIds(ids) > 0;
    }
}
