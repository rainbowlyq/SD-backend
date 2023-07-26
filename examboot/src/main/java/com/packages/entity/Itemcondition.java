package com.packages.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Itemcondition {
    @TableId
    private String docid;
    @TableId
    private String matid;
    @TableId
    private String contype;
    private Integer conamount;
}
