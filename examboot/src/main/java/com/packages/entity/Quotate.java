package com.packages.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class Quotate {
    @TableId
    private Integer quoid;
    @TableId
    private Integer matid;
    private Integer ordquantity;
    private Integer probability;
    private String description;
}
