package com.zxsc.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Product {
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private Integer stock;
    private String image;
    private String description;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private String categoryName;
}
