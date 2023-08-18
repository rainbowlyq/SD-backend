package com.packages.controller;

import com.packages.entity.Invoice;
import com.packages.entity.Salesorder;
import com.packages.mapper.InvoiceMapper;
import com.packages.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invoice")
public class InvoiceController extends BaseController<Invoice, InvoiceService, InvoiceMapper> {
    @Resource
    private InvoiceService invoiceService;
    
    @Resource
    private InvoiceMapper invoiceMapper;
    
    @GetMapping("/search")
    public List<Invoice> searchInvoice(@RequestParam Map<String, String> params){
        return invoiceService.findAllInvoices(params);
    }
    
    @PostMapping("/invalidate")
    public int invalidateInvoiceById(@RequestBody Invoice invoice){
        System.out.println(invoice.getInvId());
        try{
            invoice = invoiceService.getById(invoice);
            invoiceService.invalidateInvoice(invoice);
            return 0;
        }catch (Exception e){
            return 1;
        }
    }
    
    @PostMapping("/createByDeliveryId/{delid}")
    public Invoice createInvoiceByDeliveryId(@PathVariable int delid){
        return invoiceService.createInvoiceByDelId(delid);
    }
    @PostMapping("/createBySalOrdId/{salOrdId}")
    public Invoice createBySalOrdId(@PathVariable int salOrdId){
        return invoiceService.createInvoiceBySalOrdId(salOrdId);
    }
    
    @PostMapping("/create")
    public Invoice createInvoice(@RequestBody Invoice invoice){
        return invoiceService.createInvoice(invoice);
    }
    
    @PostMapping("/getInvoiceById/{invid}")
    public Invoice getInvoiceById(@PathVariable int invid){
        Invoice invoice = invoiceMapper.selectById(invid);
        invoice=invoiceService.updateProperties(invoice);
        return invoice;
    }
    
    @PostMapping("/getRemaining")
    public double getRemaining(@RequestBody Salesorder salesorder){
        return invoiceService.getRemaining(salesorder);
    }
}
