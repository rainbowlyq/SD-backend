package com.packages.controller;

import com.packages.entity.Customer;
import com.packages.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
@RequestMapping("/knowledge")
@RestController
public class CustomerController {
    @Resource
    CustomerService customerService;
    @GetMapping("/findAll")
    public List<Customer> test1() {
        List<Customer> all=customerService.selectList(null);
        return all;
    }
}
