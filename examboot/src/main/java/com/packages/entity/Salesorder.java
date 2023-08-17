package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    
    @TableField(exist = false)
    private List<Delivery> deliveryList;
    @TableField(exist = false)
    private List<Invoice> invoiceList;
    @TableField(exist = false)
    private final String[] delIssueList = {"NPIC", "PPIC", "APIC", "NSTA", "PSTA", "ASTA"};
    @TableField(exist = false)
    private final String[] invIssueList = {"NINV", "PINV", "AINV", "NREC", "PREC", "AREC"};
    
    public boolean updateStatus() {
        if ("NPIC".equals(delissue) && "NINV".equals(invissue)) {
            //status=ORD
            return !"ORD".equals(status);
        } else if (!"NPIC".equals(delissue) && !"ASTA".equals(delissue)) {
            //status=DLV
            boolean res = !"DLV".equals(status);
            if (res) {
                status = "DLV";
            }
            return res;
        } else if (!"NINV".equals(invissue) && !"AREC".equals(invissue)) {
            //status=INV
            boolean res = !"INV".equals(status);
            if (res) {
                status = "INV";
            }
            return res;
        } else if ("ASTA".equals(delissue) && "AREC".equals(invissue)) {
            //status=FIN
            status = "FIN";
            return true;
        } else {
            System.out.println("ISSUE ERROR");
            return false;
        }
    }
    
    public boolean updateLists() {
        int deliveryMaxStatus = Collections.max(deliveryList.stream().map(Delivery::getStatus).collect(Collectors.toList()));
        int deliveryMinStatus = Collections.min(deliveryList.stream().map(Delivery::getStatus).collect(Collectors.toList()));
        String newDelIssue = delIssueList[getIssueIndex(deliveryMinStatus, deliveryMaxStatus)];
        
        int invoiceMaxStatus = Collections.max(invoiceList.stream().map(Invoice::getStatus).collect(Collectors.toList()));
        int invoiceMinStatus = Collections.min(invoiceList.stream().map(Invoice::getStatus).collect(Collectors.toList()));
        String newInvIssue = invIssueList[getIssueIndex(invoiceMinStatus + 1, invoiceMaxStatus + 1)];
        
        if(Objects.equals(newDelIssue, delissue) && Objects.equals(newInvIssue, invissue)){
            return updateStatus();
        }else{
            delissue=newDelIssue;
            invissue=newInvIssue;
            updateStatus();
            return true;
        }
    }
    
    private int getIssueIndex(int min, int max) {
        if (min >= 3) {
            return 5;
        } else if (min == 2 && max >= 3) {
            return 4;
        } else if (max == 1) {
            return 0;
        } else if (min == 1 && max > 1) {
            return 1;
        } else {
            //or3
            return 2;
        }
    }
}
