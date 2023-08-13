package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Salesorder {
    @TableId(type = IdType.AUTO)
    private Integer salordid;
    private String sorg;
    private String dischannel;
    private String division;
    private String soldtoparty;
    private String shiptoparty;
    private String cusref;
    private String reqdelivdate;
    private Double netvalue;
    private String currency;
    private Integer weight;
    private Integer refquoid;
    private String status;
    private String invissue;
    private String delissue;
    private String createdate;
}
