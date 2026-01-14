package com.zxsc.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    private Integer status;
    private String address;
    private Date createTime;
    private Date updateTime;
    private String userName;
    private List<OrderItem> items;
}
