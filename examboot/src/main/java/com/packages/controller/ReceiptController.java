package com.packages.controller;

import com.packages.entity.Invoice;
import com.packages.entity.Receipt;
import com.packages.mapper.ReceiptMapper;
import com.packages.service.InvoiceService;
import com.packages.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipt")
public class ReceiptController extends BaseController<Receipt, ReceiptService, ReceiptMapper> {
    @Autowired ReceiptService receiptService;
    
    @PostMapping("/create")
    public void createReceiptByInvoice(@RequestBody Receipt receipt){
        receiptService.createReceipt(receipt);
    }
    
    @PostMapping("/getAmountByInvId")
    public Double getAmountByInvId(@RequestBody Invoice invoice){
        return receiptService.getAmountByInvId(invoice.getInvId());
    }
    
    @PostMapping("/findByInvId")
    public Receipt findReceiptByInvId(@RequestBody Invoice invoice){
        Receipt receipt = new Receipt();
        receipt.setInvId(invoice.getInvId());
        receipt = receiptService.search(receipt).get(0);
        return receiptService.updateProperties(receipt);
    }
}
