package com.packages.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
    private Double slevalue;

    @TableField(exist = false)
    private Double price;

    @TableField(exist = false)
    private Integer msdid;

    @TableField(exist = false)
    private String storageloc;

    @TableField(exist = false)
    private String delstorplant;
}
