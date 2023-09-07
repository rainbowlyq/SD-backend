package com.packages.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Table(name = "salesorder_detail")
public class SalesorderDetail {
    @Id
    Integer id;
    Integer salordid;
    Integer delid;
    Integer invid;
    Integer recid;
    Integer picid;
    String status;
    String invissue;
    String delissue;
    Integer delStatus;
    Integer invStatus;
    Integer matid;
    String matName;
    String matDesc;
    Integer quantity;
    String plant;
    String storageloc;
    Double conditionprice;
    Integer soldtoparty;
    Integer shiptoparty;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate reqdelivdate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate pickingDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate invoiceDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate receiptDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate giDate;
    Double netvalue;
    Double invAmount;
    Double recAmount;
    String currency;
}
