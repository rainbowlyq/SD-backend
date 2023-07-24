package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class ContactPerson {
    @TableId(type = IdType.AUTO)
    private Integer personId;
    private String title;
    private String first_name;
    private String last_name;
    private String correspondence_lang;
    private String srchterm;
    private String street;
    private Integer postal_code;
    private String country;
    private String region;
}
