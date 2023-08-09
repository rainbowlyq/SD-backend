package com.packages.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("invoice")
public class Invoice {
    @TableId(value = "invid", type = IdType.AUTO)
    private Integer invId;
    
    @TableField(value = "delid")
    private Long delId;
    
    @TableField(value = "salordid")
    private int salOrdId;
    
    @TableField(value = "shiptoparty")
    private String shipToParty;
    
    @TableField(value = "netvalue")
    private Double netValue;
    
    @TableField(value = "status")
    private int status = 1;
    
    @TableField(value = "update_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime updateDatetime;
    
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateDatetimeStart;
    
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateDatetimeEnd;
    
    @TableField(exist = false)
    private Customer shipToPartyCustomer;
    
    @TableField(exist = false)
    private Delivery delivery;
    
    @TableField(exist = false)
    private Salesorder salesorder;
    
    @TableField(exist = false)
    private List<DeliveryItem> deliveryItems = null;
    
}
