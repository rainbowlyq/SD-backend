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

@RestController
@RequestMapping("/receipt")
public class ReceiptController extends BaseController<Receipt, ReceiptService, ReceiptMapper> {
    @Autowired
    ReceiptService receiptService;
    @Autowired
    SalesOrderService salesOrderService;
    
    @PostMapping("/create")
    public void createReceiptByInvoice(@RequestBody Receipt receipt) {
        receiptService.createReceipt(receipt);
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
    
    @GetMapping("/test/{salordid}")
    public void test(@PathVariable Integer salordid) {
        Salesorder salesorder = salesOrderService.getBySalordId(salordid);
        System.out.println(salesorder.getDelissue() + salesorder.getInvissue());
        salesOrderService.updateSalesOrderStatus(salesorder);
        System.out.println(salesorder.getDelissue() + salesorder.getInvissue());
    }
}
