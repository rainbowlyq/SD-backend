package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("picking")
public class Picking {
    @TableId(value = "picid", type = IdType.AUTO)
    private Long picid;

    @TableField(value = "delid")
    private Long delid;

    @TableField(value = "matid")
    private String matid;

    @TableField(value = "quantity")
    private Integer quantity;

    @TableField(value = "plant")
    private String plant;


    @TableField(value = "storageloc")
    private String storageloc;

    @TableField(value = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;
}
