package com.packages.controller;

import com.packages.entity.Customer;
import com.packages.service.ChartService;
import com.packages.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping("/chart")
@RestController
public class ChartController {
    @Resource
    private ChartService chartService;
    @GetMapping("/getCountLine")
    public List<Map<String, Object>> getCountLine() {
        return chartService.getCountLine();
    }
    @GetMapping("/getOrderCount")
    public List<Map<String, Object>> getOrderCount() {
        return chartService.getOrderCount();
    }
}
