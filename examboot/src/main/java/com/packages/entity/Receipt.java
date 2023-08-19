package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Receipt {
    @TableId(value = "recid", type = IdType.AUTO)
    private Integer recId;
    @TableField(value = "invid")
    private Integer invId;
    private Double amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private String description;
    
    @TableField(exist = false)
    private Invoice invoice = null;
    
    @TableField(exist = false)
    private Integer salordid;
    
    @TableField(exist = false)
    private String currency;
    
    @TableField(exist = false)
    private Integer soldtoparty;
    
    @TableField(exist = false)
    private String soldtopartyName;
    
    @TableField(exist = false)
    private String startDate;
    
    @TableField(exist = false)
    private String endDate;
    
}
