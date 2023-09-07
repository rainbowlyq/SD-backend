package com.packages.controller;

import com.packages.entity.SalesorderDetail;
import com.packages.service.SalesorderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SalesorderDetailController{
    
    @Autowired
    SalesorderDetailService service;
    
    @PostMapping("/getMaterialDocuments")
    public List<SalesorderDetail> getMaterialDocuments(@RequestBody SalesorderDetail salesorderDetail){
        return service.getMaterialDocuments(salesorderDetail);
    }
}
