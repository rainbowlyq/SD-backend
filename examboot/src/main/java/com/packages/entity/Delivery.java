package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@TableName("delivery")
public class Delivery {

    @TableId(value = "delid", type = IdType.AUTO)
    private Long delid;

    @TableField(value = "salordid")
    private Integer salordid;

    @TableField(value = "shiptoparty")
    private String shiptoparty;

    @TableField(value = "status")
    private Integer status = 1;

    @TableField(value = "description")
    private String description;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickingDate;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickingDateStart;


    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickingDateEnd;

    @TableField(exist = false)
    private List<Sell> items;
}
