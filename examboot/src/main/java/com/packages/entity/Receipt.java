package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Receipt {
    @TableId(value = "recid", type = IdType.AUTO)
    private Integer recId;
    
    @TableField(value = "invid")
    private Integer invId;
    
    @TableField(value = "amount")
    private Double amount;
    
    @TableField(value = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime datetime;
    
    @TableField(value = "description")
    private String description;
    
    @TableField(exist = false)
    private Invoice invoice = null;
    
    @TableField(exist = false)
    private List<DeliveryItem> deliveryItems = null;
    
}
