package com.packages.controller;

import com.packages.service.CustomerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
@RequestMapping("/customer")
@RestController
public class CustomerController {
    @Resource
    private CustomerService customerService;
    @GetMapping("/getDivisionCombination")
    public List<Map<String, Object>> getDivisionCombination() {
        return customerService.divisionCombination();
    }
    @GetMapping("/getSearchCombination")
    public Map<String, List<Map<String, Object>>> getSearchCombination() {
        return customerService.getSearchCombination();
    }
}
