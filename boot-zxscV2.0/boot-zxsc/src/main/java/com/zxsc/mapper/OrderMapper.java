package com.zxsc.mapper;

import com.zxsc.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderMapper {
    List<Order> findAll();
    
    List<Order> findByUserId(Long userId);
    
    Order findById(Long id);
    
    Order findByOrderNo(String orderNo);
    
    int insert(Order order);
    
    int update(Order order);
    
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
