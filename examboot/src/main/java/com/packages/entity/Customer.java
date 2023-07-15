package com.packages.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class Customer {
    @TableId
    private String cusid;
    private String srchterm;
}
