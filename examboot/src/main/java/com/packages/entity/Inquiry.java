package com.packages.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class Inquiry {
    @TableId
    private String inqid;
    private String sorg;
    private String dischannel;
    private String division;
    private String soldtoparty;
    private String shiptoparty;
    private String cusref;
    private String validfrom;
    private String validto;
    private String reqdelivdate;
    private Integer expordvalue;
    private String currency;
    private Integer weight;
}
