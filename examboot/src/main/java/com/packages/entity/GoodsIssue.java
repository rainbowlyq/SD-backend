package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("goods_issue")
public class GoodsIssue {
    @TableId(value = "giid", type = IdType.AUTO)
    private Long giid;

    @TableField(value = "delid")
    private Long delid;

    @TableField(value = "date")
    private LocalDateTime date;

}
