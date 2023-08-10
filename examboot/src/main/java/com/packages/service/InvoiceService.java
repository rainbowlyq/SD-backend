package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.packages.entity.Salesorder;
import com.packages.entity.Delivery;
import com.packages.entity.DeliveryItem;
import com.packages.entity.Invoice;
import com.packages.entity.MaterialSd;
import com.packages.mapper.CustomerMapper;
import com.packages.mapper.DeliveryMapper;
import com.packages.mapper.InvoiceMapper;
import com.packages.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class InvoiceService extends BaseService<InvoiceMapper, Invoice> {
    
    @Autowired
    private SalesOrderService salesOrderService;
    
    @Autowired
    private InvoiceMapper invoiceMapper;
    
    @Autowired
    private DeliveryService deliveryService;
    
    @Autowired
    private DeliveryItemService deliveryItemService;
    
    @Autowired
    private DeliveryMapper deliveryMapper;
    
    @Autowired
    private CustomerMapper customerMapper;
    
    @Autowired
    private MaterialMapper materialMapper;
    
    //根据1个delivery开1个invoice
    public Invoice createInvoiceByDelId(int delid) {
        Invoice invoice = new Invoice();
        Delivery delivery = deliveryService.getById(delid);
        invoice.setDelId(delivery.getDelid());
        invoice.setSalOrdId(delivery.getSalordid());
        if (isWholeSalesOrder(delivery)) {
            invoice.setNetValue(salesOrderService.getBySalordId(invoice.getSalOrdId()).getNetvalue());
        }else{
            //todo
            int salordid = invoice.getSalOrdId();
            invoice.setNetValue(salesOrderService.getBySalordId(salordid).getNetvalue()/getDeliveryNum(salordid));
        }
        // todo
        invoice.setShipToParty(delivery.getShiptoparty());
        invoice.setUpdateDatetime(LocalDateTime.now());
        invoiceMapper.insert(invoice);
        return invoice;
    }
    
    //根据1个sales order开1个invoice
    public Invoice createInvoiceBySalOrdId(int salOrdId) {
        Salesorder salesorder = salesOrderService.getBySalordId(salOrdId);
        Invoice invoice = new Invoice();
        invoice.setSalOrdId(salOrdId);
        invoice.setShipToParty(salesorder.getShiptoparty());
        invoice.setNetValue(salesorder.getNetvalue());
        invoice.setUpdateDatetime(LocalDateTime.now());
        invoiceMapper.insert(invoice);
        return invoice;
    }
    public Invoice createInvoice(Invoice invoice) {
        Salesorder salesorder = salesOrderService.getBySalordId(invoice.getSalOrdId());
        List<Delivery> deliveries = deliveryService.findAllBySalOrdId(invoice.getSalOrdId());
        if(deliveries.size()==1){
            invoice.setDelId(deliveries.get(0).getDelid());
        }
        // Double netvalue = 0.0;
        // List<Delivery> deliveries = deliveryService.findAllBySalOrdId();
        // for (Delivery delivery : deliveries) {
        //     netvalue += salesOrderService.getBySalordId(delivery.getSalordid()).getNetvalue();
        // }
        invoice.setUpdateDatetime(LocalDateTime.now());
        invoiceMapper.insert(invoice);
        return invoice;
    }
    
    
    //作废Invoice
    public void invalidateInvoice(Invoice invoice) {
        invoice.setStatus(0);
        invoiceMapper.updateById(invoice);
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
    
    // public List<Delivery> getBillingDueList(){
    //
    //
    // }
    
    // 检查delivery是否属于分批发货
    public boolean isWholeSalesOrder(Delivery delivery) {
        int salordid = delivery.getSalordid();
        List<Delivery> deliveries = deliveryService.findAllBySalOrdId(salordid);
        return deliveries.size() == 1;
    }
    
    public int getDeliveryNum(int salordid){
        List<Delivery> deliveries = deliveryService.findAllBySalOrdId(salordid);
        return deliveries.size();
    }
    
    public Invoice updateProperties(Invoice invoice){
        invoice.setShipToPartyCustomer(customerMapper.selectById(invoice.getShipToParty()));
        invoice.setDelivery(deliveryMapper.selectById(invoice.getDelId()));
        DeliveryItem d = new DeliveryItem();
        d.setDelid(invoice.getDelId());
        invoice.setDeliveryItems(deliveryItemService.search(d));
        invoice.setSalesorder(salesOrderService.getBySalordId(invoice.getSalOrdId()));
        for (DeliveryItem deliveryItem : invoice.getDeliveryItems()) {
            deliveryItem.setCurrency(salesOrderService.getBySalordId(invoice.getSalOrdId()).getCurrency());
            MaterialSd material = materialMapper.selectById(deliveryItem.getMatid());
            if (material != null) {
                deliveryItem.setWeight(material.getWeight());
                deliveryItem.setWeightUnit(material.getWeightunit());
            }
        }
        return invoice;
    }
    
}

