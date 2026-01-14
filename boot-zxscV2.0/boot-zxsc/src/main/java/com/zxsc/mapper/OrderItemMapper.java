package com.zxsc.mapper;

import com.zxsc.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface OrderItemMapper {
    List<OrderItem> findByOrderId(Long orderId);
    
    int insert(OrderItem orderItem);
    
    int batchInsert(List<OrderItem> items);
}
