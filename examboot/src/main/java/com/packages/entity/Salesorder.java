package com.packages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.ExcludeDefaultListeners;
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
    private List<Sell> sellList;
    @TableField(exist = false)
    private final String[] delIssueList = {"NPIC", "PPIC", "APIC", "NSTA", "PSTA", "ASTA"};
    @TableField(exist = false)
    private final String[] invIssueList = {"NINV", "PINV", "AINV", "NREC", "PREC", "AREC"};
    
    public boolean updateStatus() {
        if ("NPIC".equals(delissue) && "NINV".equals(invissue)) {
            //status=ORD
            return !"ORD".equals(status);
        } else if ((!"NPIC".equals(delissue)) && (!"ASTA".equals(delissue))) {
            //status=DLV
            boolean res = !"DLV".equals(status);
            if (res) {
                status = "DLV";
            }
            return res;
        } else if (!"AREC".equals(invissue)) {
            //status=INV
            boolean res = !"INV".equals(status);
            if (res) {
                status = "INV";
            }
            return res;
        } else if ("ASTA".equals(delissue)) {
            //status=FIN
            status = "FIN";
            return true;
        } else {
            System.out.println("ISSUE ERROR");
            System.out.println(delissue);
            System.out.println(invissue);
            return false;
        }
    }
    
    public boolean updateLists() {
        //判断是否存在未发货的商品
        boolean notPicked = false;
        for (Sell sell : sellList) {
            int countedItems = 0;
            for (Delivery delivery : deliveryList) {
                for (Picking picking : delivery.getPickings()) {
                    if (picking.getMatid().equals(sell.getMatid())) {
                        countedItems += picking.getQuantity();
                    }
                }
            }
            if (countedItems < sell.getOrdquantity()) {
                notPicked = true;
                break;
            }
        }
        
        //判断是都存在未开票的商品
        double invoicedAmount = 0;
        for (Invoice invoice : invoiceList) {
            if (invoice.getStatus() > 0) {
                invoicedAmount += invoice.getNetValue();
            }
        }
        boolean notInvoiced = invoicedAmount < netvalue;
        
        List<Integer> deliveryStatusList = deliveryList.stream().map(Delivery::getStatus).collect(Collectors.toList());
        List<Integer> invoiceStatusList = invoiceList.stream().map(Invoice::getStatus).collect(Collectors.toList());
        invoiceStatusList = invoiceStatusList.stream().filter(status -> status > 0).collect(Collectors.toList());
        int deliveryMinStatus = 1;
        int deliveryMaxStatus = 1;
        int invoiceMaxStatus = 0;
        int invoiceMinStatus = 0;
        if (deliveryStatusList.size() > 0) {
            deliveryMaxStatus = Collections.max(deliveryStatusList);
            deliveryMinStatus = Collections.min(deliveryStatusList);
        }
        if (notPicked) {
            deliveryMinStatus = 1;
        }
        
        String newDelIssue = delIssueList[getIssueIndex(deliveryMinStatus, deliveryMaxStatus)];
        if (invoiceStatusList.size() > 0) {
            invoiceMaxStatus = Collections.max(invoiceStatusList);
            invoiceMinStatus = Collections.min(invoiceStatusList);
        }
        if (notInvoiced) {
            invoiceMinStatus = 0;
        }
        System.out.println(notPicked);
        System.out.println(notInvoiced);
        System.out.println(deliveryMinStatus);
        System.out.println(deliveryMinStatus);
        System.out.println(invoiceMinStatus);
        System.out.println(invoiceMaxStatus);
        String newInvIssue = invIssueList[getIssueIndex(invoiceMinStatus + 1, invoiceMaxStatus + 1)];
        System.out.println(newDelIssue);
        System.out.println(newInvIssue);
        if (Objects.equals(newDelIssue, delissue) && Objects.equals(newInvIssue, invissue)) {
            return updateStatus();
        } else {
            delissue = newDelIssue;
            invissue = newInvIssue;
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
            //or 3
            return 2;
        }
    }
}
