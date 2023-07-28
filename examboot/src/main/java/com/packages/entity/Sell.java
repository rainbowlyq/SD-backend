package com.packages.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Sell {
    @TableId
    private Integer salordid;
    @TableId
    private Integer matid;
    private Integer ordquantity;
    private String description;
}
