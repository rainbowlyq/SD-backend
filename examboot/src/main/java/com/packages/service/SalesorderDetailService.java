package com.packages.service;

import com.packages.entity.SalesorderDetail;
import com.packages.mapper.SalesorderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesorderDetailService extends BaseService<SalesorderDetailMapper, SalesorderDetail> {
    
    @Autowired
    private SalesorderDetailService salesorderDetailService;
    
    public List<SalesorderDetail> getMaterialDocuments(SalesorderDetail salesorderDetail) {
        return salesorderDetailService.search(salesorderDetail);
    }
}

