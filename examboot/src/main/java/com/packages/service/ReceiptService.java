package com.packages.service;

import com.packages.entity.*;
import com.packages.mapper.InvoiceMapper;
import com.packages.mapper.MaterialMapper;
import com.packages.mapper.ReceiptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptService extends BaseService<ReceiptMapper, Receipt> {
    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private InvoiceMapper invoiceMapper;
    
    @Autowired
    private PickingService pickingService;
    
    @Autowired
    private SalesOrderService salesOrderService;
    
    public void createReceipt(Receipt receipt) {
        receipt.setDatetime(LocalDateTime.now());
        save(receipt);
        Invoice invoice = invoiceService.getById(receipt.getInvId());
        if (receipt.getAmount().equals(invoice.getNetValue())) {
            invoice.setStatus(2);
            salesOrderService.updateSalesOrderStatus(salesOrderService.getById(invoice.getSalOrdId()));
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
        Picking d = new Picking();
        d.setDelid(invoice.getDelId());
        List<Picking> pickingList=pickingService.search(d);
        pickingList = pickingList.stream()
                .map(pic->pickingService.updateProperties(pic))
                .collect(Collectors.toList());
        receipt.setItems(pickingList);
        return receipt;
    }
}
