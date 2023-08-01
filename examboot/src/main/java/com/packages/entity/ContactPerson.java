package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class ContactPerson {
    @TableId(type = IdType.AUTO)
    private Integer personId;   //contactPerson的BP号
    private String title;   //Mr.\Miss等称号
    private String firstName;   //名
    private String lastName;    //姓
    private String correspondenceLang;  //交流语言
    private String srchterm;    //search term
    private String street;  //所属街道
    private Integer postalCode; //邮编
    private String country; //国家
    private String region;  //地区
}
