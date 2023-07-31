package com.packages.controller;

import com.packages.entity.DeliveryItem;
import com.packages.mapper.DeliveryItemMapper;
import com.packages.service.DeliveryItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery/item")
public class DeliveryItemController extends BaseController<DeliveryItem, DeliveryItemService, DeliveryItemMapper> {
}
