package com.packages.controller;

import com.packages.entity.Inquiry;
import com.packages.service.InquiryService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping("/inquiry")
@RestController
public class InquiryController {
    @Resource
    private InquiryService inquiryService;

    @GetMapping("/findAll")
    public List<Inquiry> findAll(@RequestParam MultiValueMap<String, String> params) {
        return inquiryService.findAllInquiries(params);
    }
    @GetMapping("/getInquiryDetail")
    public Inquiry getInquiryDetail(@RequestParam("inqid") String inqid) {
        return inquiryService.findInquiryByInqid(inqid);
    }
    @PostMapping("/update")
    public int update(@RequestBody Inquiry inquiry) {
        return inquiryService.updateInquiry(inquiry);
    }

    @GetMapping("/testJDBC")
    public List<Map<String, Object>> testJDBC(@RequestParam("inqid") String inqid) {
        return inquiryService.testJDBC(inqid);
    }
}
