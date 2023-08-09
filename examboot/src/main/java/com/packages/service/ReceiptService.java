package com.packages.service;

import com.packages.entity.DeliveryItem;
import com.packages.entity.Invoice;
import com.packages.entity.MaterialSd;
import com.packages.entity.Receipt;
import com.packages.mapper.InvoiceMapper;
import com.packages.mapper.MaterialMapper;
import com.packages.mapper.ReceiptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService extends BaseService<ReceiptMapper, Receipt> {
    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private InvoiceMapper invoiceMapper;
    
    @Autowired
    private DeliveryItemService deliveryItemService;
    
    @Autowired
    private MaterialMapper materialMapper;
    
    @Autowired
    private SalesOrderService salesOrderService;
    
    public void createReceipt(Receipt receipt) {
        save(receipt);
        Invoice invoice = invoiceService.getById(receipt.getInvId());
        if (receipt.getAmount().equals(invoice.getNetValue())) {
            invoice.setStatus(2);
            //todo 修改salord status
        }
        invoiceService.saveOrUpdate(invoice);
    }
    
    public Double getAmountByInvId(Integer invId) {
        Invoice invoice = invoiceService.getById(invId);
        return invoice.getNetValue();
    }
    
    public Receipt updateProperties(Receipt receipt) {
        Invoice invoice = invoiceMapper.selectById(receipt.getInvId());
        receipt.setInvoice(invoiceService.updateProperties(invoice));
        DeliveryItem d = new DeliveryItem();
        d.setDelid(invoice.getDelId());
        receipt.setDeliveryItems(deliveryItemService.search(d));
        for (DeliveryItem deliveryItem : receipt.getDeliveryItems()) {
            deliveryItem.setCurrency(salesOrderService.getBySalordId(invoice.getSalOrdId()).getCurrency());
            MaterialSd material = materialMapper.selectById(deliveryItem.getMatid());
            if (material != null) {
                deliveryItem.setWeight(material.getWeight());
                deliveryItem.setWeightUnit(material.getWeightunit());
            }
        }
        return receipt;
    }
}
