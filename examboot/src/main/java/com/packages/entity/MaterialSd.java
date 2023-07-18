package com.packages.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class MaterialSd {
    @TableId
    private Integer msdId;
    private String mid;
    private String salesOrg;
    private String distrChannel;
    private String currency;
    private Double price;
    private String division;
    private String delStorPlant;
    private String storageLoc;
    private String descrp;
    private String bomUnit;
    private Double weight;
    private String weightUnit;
    private String category;
    private String mtype;
}