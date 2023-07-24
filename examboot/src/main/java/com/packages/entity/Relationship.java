package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Relationship {
    @TableId(type = IdType.AUTO)
    private Integer relationship_number;
    private Integer business_partner1;
    private Integer business_partner2;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date valid_from;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date valid_to;
    private Integer vip;
    private Integer department;
    private String relation_category;
}
