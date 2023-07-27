package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class ContactPerson {
    @TableId(type = IdType.AUTO)
    private Integer personId;
    private String title;
    private String firstName;
    private String lastName;
    private String correspondenceLang;
    private String srchterm;
    private String street;
    private Integer postalCode;
    private String country;
    private String region;
}
