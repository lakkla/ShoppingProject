package com.zxsc.service;

import com.zxsc.entity.Cart;
import com.zxsc.entity.Order;
import com.zxsc.entity.OrderItem;
import com.zxsc.entity.Product;
import com.zxsc.mapper.OrderItemMapper;
import com.zxsc.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductService productService;
    
    public List<Order> findAll() {
        List<Order> orders = orderMapper.findAll();
        for (Order order : orders) {
            order.setItems(orderItemMapper.findByOrderId(order.getId()));
        }
        return orders;
    }
    
    public List<Order> findByUserId(Long userId) {
        List<Order> orders = orderMapper.findByUserId(userId);
        for (Order order : orders) {
            order.setItems(orderItemMapper.findByOrderId(order.getId()));
        }
        return orders;
    }
    
    public Order findById(Long id) {
        Order order = orderMapper.findById(id);
        if (order != null) {
            order.setItems(orderItemMapper.findByOrderId(id));
        }
        return order;
    }
    
    @Transactional
    public Order createOrder(Long userId, List<Long> cartIds, String address) {
        List<Cart> carts = cartService.findByUserId(userId);
        List<Cart> selectedCarts = new ArrayList<>();
        for (Cart cart : carts) {
            if (cartIds.contains(cart.getId())) {
                selectedCarts.add(cart);
            }
        }
        
        if (selectedCarts.isEmpty()) {
            return null;
        }
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        
        for (Cart cart : selectedCarts) {
            Product product = productService.findById(cart.getProductId());
            if (product == null || product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("商品库存不足");
            }
            
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(cart.getQuantity());
            items.add(item);
            
            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            productService.updateStock(product.getId(), cart.getQuantity());
        }
        
        Order order = new Order();
        order.setOrderNo("ORD" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        order.setAddress(address);
        
        orderMapper.insert(order);
        
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
        }
        orderItemMapper.batchInsert(items);
        
        cartService.deleteByIds(cartIds);
        
        return order;
    }
    
    public boolean updateStatus(Long id, Integer status) {
        return orderMapper.updateStatus(id, status) > 0;
    }
    
    public boolean cancelOrder(Long id) {
        Order order = orderMapper.findById(id);
        if (order == null || order.getStatus() != 0) {
            return false;
        }
        return orderMapper.updateStatus(id, 4) > 0;
    }
}
