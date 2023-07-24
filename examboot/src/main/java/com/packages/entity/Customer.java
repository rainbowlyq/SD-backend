package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class Customer {
    @TableId(type = IdType.AUTO)
    private Integer bp;
    private Integer bpId;
    private String srchterm;
    private Integer sortKey;
    private String currency;
    private Integer priceGroup;
    private Integer custPricProcedure;
    private String deliveringPlant;
    private Integer deliveryPriority;
    private Integer shippingConditions;
    private Integer maxPartDeliveries;
    private String incoterms;
    private String incotermsLocation1;
    private Integer paymentTerms;
    private Integer acctAssmtGrpCust;
    private Integer taxClassific;
    private String title;
    private String name;
    private String street;
    private Integer postalCode;
    private String city;
    private String country;
    private String region;
    private String transportationZone;
    private String language;
    private String companyCode;
    private Integer reconciliationAcct;
    private String saleOrg;
    private String distrChannel;
    private String division;
}
