package com.packages.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class MaterialSd {
    @TableId(type = IdType.AUTO)
    private Integer msdid;  //主键，标识该物料
    private String mid; //物料编号
    private String salesorg;    //销售组织
    private String distrchannel;    //分销渠道
    private String currency;    //货币
    private Double price;
    private String division;    //分区
    private String delstorplant;
    private String storageloc;  //储存位置
    private String descrp;  //形容
    private String bomunit;
    private Double weight;
    private String weightunit;
    private String category;
    private String mtype;
}