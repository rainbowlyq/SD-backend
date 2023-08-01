package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Relationship {
    @TableId(type = IdType.AUTO)
    private Integer relationshipNumber; //主键，唯一标识customer和contactPerson的关系
    private Integer businessPartner1;   //顾客（customer）ID
    private Integer businessPartner2;   //contactPerson的ID
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date validFrom; //该Relationship的有效日期开始
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date validTo;   //该Relationship的有效日期结束
    private Integer vip;    //contactPerson的management
    private Integer department; //contactPerson部门
    private String relationCategory;    //关系类型
}
