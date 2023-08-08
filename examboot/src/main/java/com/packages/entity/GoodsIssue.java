package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@TableName("goods_issue")
public class GoodsIssue {
    @TableId(value = "giid", type = IdType.AUTO)
    private Long giid;

    @TableField(value = "delid")
    private Long delid;

    @TableField(value = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @TableField(exist = false)
    private Map<String, Object> order;

    @TableField(exist = false)
    private List<Map<String, Object>> items;

}
