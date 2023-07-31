package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("delivery_item")
public class DeliveryItem {
    @TableId(value = "diid", type = IdType.AUTO)
    private Long diid;

    @TableField(value = "delid")
    private Long delid;

    @TableField(value = "matid")
    private String matid;

    @TableField(value = "quantity")
    private Integer quantity;

    @TableField(value = "avgvalue")
    private Double avgvalue;

}
