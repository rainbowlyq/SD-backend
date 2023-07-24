package com.packages.controller;

import com.packages.entity.Customer;
import com.packages.entity.Inquiry;
import com.packages.service.CustomerService;

import com.packages.service.ContactPersonService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
@RequestMapping("/customer")
@RestController
public class CustomerController {
    @Resource
    private CustomerService customerService;
    private ContactPersonService personService;
    @GetMapping("/getDivisionCombination")
    public List<Map<String, Object>> getDivisionCombination() {
        return customerService.divisionCombination();
    }
    @GetMapping("/getSearchCombination")
    public Map<String, List<Map<String, Object>>> getSearchCombination() {
        return customerService.getSearchCombination();
    }
    @GetMapping("/getCustomers")
    public List<Customer> getCustomers(@RequestParam Map<String, String> params) {
        return customerService.findAllCustomers(params);
    }
    @PostMapping("/insert")
    public int insert(@RequestBody Customer customer) {
        return customerService.insertcustomer(customer);
    }
}
