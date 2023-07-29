package com.packages.controller;

import com.packages.entity.Delivery;
import com.packages.mapper.DeliveryMapper;
import com.packages.service.DeliveryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
public class DeliveryController extends BaseController<Delivery, DeliveryService, DeliveryMapper> {
}
