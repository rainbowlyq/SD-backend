package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.packages.entity.*;
import com.packages.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService extends BaseService<InvoiceMapper, Invoice> {
    
    @Autowired
    private SalesOrderService salesOrderService;
    
    @Autowired
    private SellService sellService;
    
    @Autowired
    private InvoiceMapper invoiceMapper;
    
    @Autowired
    private DeliveryService deliveryService;
    
    @Autowired
    private PickingService pickingService;
    
    @Autowired
    private DeliveryMapper deliveryMapper;
    
    @Autowired
    private CustomerMapper customerMapper;
    
    @Autowired
    private PickingMapper pickingMapper;
    
    //根据1个delivery开1个invoice
    public Invoice createInvoiceByDelId(int delid) {
        Invoice invoice = new Invoice();
        invoice.setStatus(1);
        Delivery delivery = deliveryService.getById(delid);
        delivery.setStatus(4);
        deliveryMapper.updateById(delivery);
        invoice.setDelId(delivery.getDelid());
        invoice.setSalOrdId(delivery.getSalordid());
        // if (isWholeSalesOrder(delivery)) {
        //     // 未分批发货直接使用salesorder的netvalue
        //     invoice.setNetValue(salesOrderService.getBySalordId(invoice.getSalOrdId()).getNetvalue());
        // } else {
        // 计算订单折扣比例
        Sell sell = new Sell();
        sell.setSalordid(invoice.getSalOrdId());
        List<Sell> sells = sellService.search(sell);
        Double beforeDiscount = 0.;
        for (Sell s : sells) {
            beforeDiscount += s.getConditonprice() * s.getOrdquantity();
        }
        double discountRate = salesOrderService.getBySalordId(invoice.getSalOrdId()).getNetvalue() / beforeDiscount;
        
        // 计算均摊后价格
        Map<String, Object> map = new HashMap<>();
        map.put("delid", invoice.getDelId());
        List<Picking> pickings = pickingMapper.selectByMap(map);
        double netValue = 0.;
        for (Picking picking : pickings) {
            Sell s = new Sell();
            s.setSalordid(invoice.getSalOrdId());
            s.setMatid(picking.getMatid());
            sells = sellService.search(s);
            if (sells.size() == 1) {
                sell = sells.get(0);
                netValue += sell.getConditonprice() * picking.getQuantity();
            } else {
                System.out.println("more than 1 sell");
                return invoice;
            }
        }
        invoice.setNetValue(netValue * discountRate);
        // }
        // todo
        invoice.setShipToParty(delivery.getShiptoparty());
        invoice.setUpdateDatetime(LocalDateTime.now());
        if (invoice.getNetValue() > 0) {
            invoiceMapper.insert(invoice);
            salesOrderService.updateSalesOrderStatus(salesOrderService.getById(delivery.getSalordid()));
        }
        return invoice;
    }
    
    //根据1个sales order开1个invoice
    public Invoice createInvoiceBySalOrdId(int salOrdId) {
        Salesorder salesorder = salesOrderService.getBySalordId(salOrdId);
        Invoice invoice = new Invoice();
        invoice.setStatus(1);
        invoice.setSalOrdId(salOrdId);
        invoice.setShipToParty(salesorder.getShiptoparty());
        invoice.setNetValue(getRemaining(salesorder));
        invoice.setUpdateDatetime(LocalDateTime.now());
        invoiceMapper.insert(invoice);
        for (Delivery delivery : deliveryService.findAllBySalOrdId(salOrdId)) {
            if (delivery.getStatus() < 4) {
                delivery.setStatus(4);
                deliveryMapper.updateById(delivery);
            }
        }
        salesOrderService.updateSalesOrderStatus(salesorder);
        return invoice;
    }
    
    // 根据表单信息创建invoice
    public Invoice createInvoice(Invoice invoice) {
        Salesorder salesorder = salesOrderService.getBySalordId(invoice.getSalOrdId());
        // todo
        if (getRemaining(salesorder) - invoice.getNetValue() < 0.01) {
            invoice.setNetValue(getRemaining(salesorder));
            List<Delivery> deliveries = deliveryService.findAllBySalOrdId(invoice.getSalOrdId());
            for (Delivery delivery : deliveries) {
                delivery.setStatus(4);
                deliveryMapper.updateById(delivery);
            }
            if (deliveries.size() == 1) {
                invoice.setDelId(deliveries.get(0).getDelid());
            }
        }
        invoice.setUpdateDatetime(LocalDateTime.now());
        invoiceMapper.insert(invoice);
        salesOrderService.updateSalesOrderStatus(salesorder);
        return invoice;
    }
    
    //作废Invoice
    public void invalidateInvoice(Invoice invoice) {
        invoice.setStatus(0);
        invoiceMapper.updateById(invoice);
        invoice = updateProperties(invoice);
        if (invoice.getDelId() != null) {
            Delivery delivery = invoice.getDelivery();
            if (delivery.getStatus() == 4) {
                delivery.setStatus(3);
                deliveryService.updateById(delivery);
            }
        } else {
            Salesorder salesorder = invoice.getSalesorder();
            salesorder = salesOrderService.setLists(salesorder);
            List<Long> invoicedDelIds = salesorder.getInvoiceList().stream().map(Invoice::getDelId).filter(Objects::nonNull).collect(Collectors.toList());
            for (Delivery delivery : salesorder.getDeliveryList()) {
                if(!invoicedDelIds.contains(delivery.getDelid())){
                    if(delivery.getStatus()>=4){
                        delivery.setStatus(3);
                        deliveryService.updateById(delivery);
                    }
                }
            }
        }
        salesOrderService.updateSalesOrderStatus(invoice.getSalesorder());
    }
    
    public List<Invoice> findAllInvoices(Map<String, String> params) {
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<>();
        // 根据参数构建查询条件
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            if (!Objects.equals(paramValue, "")) {
                queryWrapper.eq(paramName, paramValue);
            }
        }
        return invoiceMapper.selectList(queryWrapper);
    }
    
    public List<Invoice> findAllInvoices(Map<String, String> params, boolean details) {
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<>();
        // 根据参数构建查询条件
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            if (!Objects.equals(paramValue, "")) {
                queryWrapper.eq(paramName, paramValue);
            }
        }
        List<Invoice> invoices = invoiceMapper.selectList(queryWrapper);
        if (details) {
            invoices = invoices.stream()
                    .map(this::updateProperties)
                    .collect(Collectors.toList());
        }
        return invoices;
    }
    
    // 检查delivery是否属于分批发货
    public boolean isWholeSalesOrder(Delivery delivery) {
        int salordid = delivery.getSalordid();
        List<Delivery> deliveries = deliveryService.findAllBySalOrdId(salordid);
        return deliveries.size() == 1;
    }
    
    public int getDeliveryNum(int salordid) {
        List<Delivery> deliveries = deliveryService.findAllBySalOrdId(salordid);
        return deliveries.size();
    }
    
    public Invoice updateProperties(Invoice invoice) {
        invoice.setShipToPartyCustomer(customerMapper.selectById(invoice.getShipToParty()));
        Salesorder salesorder = salesOrderService.getById(invoice.getSalOrdId());
        invoice.setSalesorder(salesorder);
        invoice.setSoldToPartyCustomer(customerMapper.selectById(salesorder.getSoldtoparty()));
        if (invoice.getDelId() != null) {
            invoice.setDelivery(deliveryMapper.selectById(invoice.getDelId()));
            Picking p = new Picking();
            p.setDelid(invoice.getDelId());
            List<Picking> pickingList = pickingService.search(p);
            pickingList = pickingList.stream()
                    .map(pic -> pickingService.updateProperties(pic))
                    .collect(Collectors.toList());
            invoice.setPickings(pickingList);
        }
        return invoice;
    }
    
    public double getRemaining(Salesorder salesorder) {
        salesorder = salesOrderService.getById(salesorder);
        Invoice invoice = new Invoice();
        invoice.setSalOrdId(salesorder.getSalordid());
        List<Invoice> invoices = search(invoice);
        Double invoiced = 0.;
        for (Invoice i : invoices) {
            if (i.getStatus() > 0) {
                invoiced += i.getNetValue();
            }
        }
        return salesorder.getNetvalue() - invoiced;
    }
}

