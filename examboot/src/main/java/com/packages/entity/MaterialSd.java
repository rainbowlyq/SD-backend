package com.packages.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class MaterialSd {
    @TableId(type = IdType.AUTO)
    private Integer msdid;
    private String mid;
    private String salesorg;
    private String distrchannel;
    private String currency;
    private Double price;
    private String division;
    private String delstorplant;
    private String storageloc;
    private String descrp;
    private String bomunit;
    private Double weight;
    private String weightunit;
    private String category;
    private String mtype;
}