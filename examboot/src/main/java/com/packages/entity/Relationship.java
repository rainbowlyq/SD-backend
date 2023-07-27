package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Relationship {
    @TableId(type = IdType.AUTO)
    private Integer relationshipNumber;
    private Integer businessPartner1;
    private Integer businessPartner2;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date validFrom;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date validTo;
    private Integer vip;
    private Integer department;
    private String relationCategory;
}
