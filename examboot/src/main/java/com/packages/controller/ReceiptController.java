package com.packages.controller;

import com.packages.entity.Invoice;
import com.packages.entity.Receipt;
import com.packages.entity.Salesorder;
import com.packages.mapper.ReceiptMapper;
import com.packages.service.InvoiceService;
import com.packages.service.ReceiptService;
import com.packages.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController extends BaseController<Receipt, ReceiptService, ReceiptMapper> {
    @Autowired
    ReceiptService receiptService;
    @Autowired
    SalesOrderService salesOrderService;
    @Autowired
    ReceiptMapper receiptMapper;
    
    @PostMapping("/create")
    public Receipt createReceiptByInvoice(@RequestBody Receipt receipt) {
        return receiptService.createReceipt(receipt);
    }
    
    @PostMapping("/getAmountByInvId")
    public Double getAmountByInvId(@RequestBody Invoice invoice) {
        return receiptService.getAmountByInvId(invoice.getInvId());
    }
    
    @PostMapping("/findByInvId")
    public Receipt findReceiptByInvId(@RequestBody Invoice invoice) {
        Receipt receipt = new Receipt();
        receipt.setInvId(invoice.getInvId());
        receipt = receiptService.search(receipt).get(0);
        return receiptService.updateProperties(receipt);
    }
    
    @PostMapping("/findAll")
    public List<Receipt> findAll(@RequestBody Receipt receipt){
        System.out.println(receiptMapper.findAll(receipt.getStartDate(),receipt.getEndDate()));
        return receiptMapper.findAll(receipt.getStartDate(),receipt.getEndDate());
    }
}
