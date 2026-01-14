package com.zxsc.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Cart {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Date createTime;
    private Date updateTime;
    private String productName;
    private BigDecimal price;
    private String image;
    private Integer stock;
}
