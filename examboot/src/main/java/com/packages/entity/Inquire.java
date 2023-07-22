package com.packages.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Inquire {
    @TableId
    private Integer inqid;
    @TableId
    private Integer matid;
    private Integer ordquantity;
    private Integer probability;
    private String description;
}