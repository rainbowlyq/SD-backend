package com.packages.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Doccondition {
    @TableId
    private String docid;
    @TableId
    private String contype;
    private Integer conamount;
}
