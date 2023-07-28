package com.packages.controller;


import com.packages.entity.Doccondition;
import com.packages.entity.Itemcondition;
import com.packages.entity.Quotate;
import com.packages.entity.Quotation;
import com.packages.service.QuotationService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RequestMapping("/quotation")
@RestController
public class QuotationController {
    @Resource
    private QuotationService quotationService;

    @GetMapping("/findAll")
    public List<Quotation> findAll(@RequestParam MultiValueMap<String, String> params) {
        return quotationService.findAllQuotations(params);
    }
    @GetMapping("/getQuotationDetail")
    public Quotation getQuotationDetail(@RequestParam("quoid") String quoid) {
        return quotationService.findQuotationByQuoid(quoid);
    }
    @PostMapping("/update")
    public int update(@RequestBody Quotation quotation) {
        return quotationService.updateQuotation(quotation);
    }
    @PostMapping("/insert")
    public int insert(@RequestBody Quotation quotation) {
        return quotationService.insertQuotation(quotation);
    }

    @PostMapping("/updateItem")
    public int updateItem(@RequestBody List<Quotate> quotates) {
        return quotationService.updateQuotate(quotates);
    }
    @PostMapping("/insertItem")
    public int insertItem(@RequestBody List<Quotate> quotates) {
        return quotationService.insertQuotate(quotates);
    }
    @GetMapping("/testJDBC")
    public List<Map<String, Object>> testJDBC(@RequestParam("quoid") String quoid) {
        return quotationService.testJDBC(quoid);
    }
    @GetMapping("/getQuotationItem")
    public List<Map<String, Object>> getQuotationItem(@RequestParam("quoid") String quoid) {
        return quotationService.findQuotationItemByQuoid(quoid);
    }
    @PostMapping("/insertItemCondition")
    public int insertItemCondition(@RequestBody List<Itemcondition> itemconditions) {
        return quotationService.insertItemCondition(itemconditions);
    }
    @PostMapping("/updateItemCondition")
    public int updateItemCondition(@RequestBody List<Itemcondition> itemconditions) {
        return quotationService.updateItemCondition(itemconditions);
    }
    @GetMapping("/getItemCondition")
    public List<Itemcondition> getItemCondition(@RequestParam("docid") String docid) {
        return quotationService.findItemCondition(docid);
    }
    @PostMapping("/insertDocCondition")
    public int insertDocCondition(@RequestBody List< Doccondition > docconditions) {
        return quotationService.insertDocCondition(docconditions);
    }
    @PostMapping("/updateDocCondition")
    public int updateDocCondition(@RequestBody List< Doccondition > docconditions) {
        return quotationService.updateDocCondition(docconditions);
    }
    @GetMapping("/getDocCondition")
    public List< Doccondition>  getDocCondition(@RequestParam("docid") String docid) {
        return quotationService.findDocCondition(docid);
    }
    @GetMapping("/setIsRefed")
    public int setIsRefed(@RequestParam("quoid") String quoid) {
        return quotationService.setIsRefed(quoid);
    }
    @GetMapping("/getSearchCombination")
    public Map<String, List<Map<String, Object>>>  getSearchCombination() {
        return quotationService.getSearchCombination();
    }
}
