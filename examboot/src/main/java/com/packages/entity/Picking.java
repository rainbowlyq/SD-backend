package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("picking")
public class Picking {
    @TableId(value = "picid", type = IdType.AUTO)
    private Long picid;
    
    @TableField(value = "delid")
    private Long delid;
    
    @TableField(value = "matid")
    private Integer matid;
    
    @TableField(value = "quantity")
    private Integer quantity;
    
    @TableField(value = "plant")
    private String plant;
    
    @TableField(value = "storageloc")
    private String storageloc;
    
    @TableField(value = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    @TableField(exist = false)
    private double conditionPrice;
    
    @TableField(exist = false)
    private String currency;
    
    @TableField(exist = false)
    private double weight;
    
    @TableField(exist = false)
    private String weightUnit;
    
    
}
