package com.packages.controller;

import com.packages.entity.Quotate;
import com.packages.entity.Quotation;
import com.packages.entity.Salesorder;
import com.packages.entity.Sell;
import com.packages.service.QuotationService;
import com.packages.service.SalesOrderService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/salesorder")
@RestController
public class SalesOrderController {
    @Resource
    private SalesOrderService salesOrderService;

    @GetMapping("/findAll")
    public List<Salesorder> findAll(@RequestParam MultiValueMap<String, String> params) {
        return salesOrderService.findAllSaleOrders(params);
    }
    @GetMapping("/getSalesOrderDetail")
    public Salesorder getSalesOrderDetail(@RequestParam("salordid") String salordid) {
        return salesOrderService.findSalesOrderByQuoid(salordid);
    }
    @PostMapping("/update")
    public int update(@RequestBody Salesorder salesorder) {
        return salesOrderService.updateSalesOrder(salesorder);
    }
    @PostMapping("/insert")
    public int insert(@RequestBody Salesorder salesorder) {
        return salesOrderService.insertSalesOrder(salesorder);
    }

    @PostMapping("/updateItem")
    //public int updateItem(@RequestBody List<Sell> sells) {return salesOrderService.updateSell(sells);}
    public int updateItem(@RequestBody Sell sell) {
        return salesOrderService.updateSell(sell);
    }
    @PostMapping("/insertItem")
    //public int insertItem(@RequestBody List<Sell> sells) {return salesOrderService.insertSell(sells);}
    public int insertItem(@RequestBody Sell sell) {
        return salesOrderService.insertSell(sell);
    }
    @GetMapping("/getSalesOrderItem")
    public List<Map<String, Object>> getSalesOrderItem(@RequestParam("salordid") String salordid) {
        return salesOrderService.findSalesOrderItemBySalordid(salordid);
    }
    @GetMapping("/getSearchCombination")
    public Map<String, List<Map<String, Object>>>  getSearchCombination() {
        return salesOrderService.getSearchCombination();
    }

    @GetMapping("/getfulall")
    public List<Map<String,Object>> getfulall(){
        List<Map<String,Object>> list=salesOrderService.findfulfillment();
        return list;
    }
}
