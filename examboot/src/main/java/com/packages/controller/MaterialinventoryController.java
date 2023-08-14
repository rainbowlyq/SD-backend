package com.packages.controller;

import com.packages.entity.Quotate;
import com.packages.entity.Quotation;
import com.packages.entity.Salesorder;
import com.packages.entity.Sell;
import com.packages.service.MaterialinventoryService;
import com.packages.service.QuotationService;
import com.packages.service.SalesOrderService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/materialinv")
@RestController
public class MaterialinventoryController {
    @Resource
    private MaterialinventoryService materialinventoryService;
}
