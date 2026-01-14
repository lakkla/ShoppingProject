package com.zxsc.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Category {
    private Long id;
    private String name;
    private Integer sort = 0;
    private Integer status = 1;
    private Date createTime;
}
