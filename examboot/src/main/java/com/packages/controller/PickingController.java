package com.packages.controller;

import com.packages.entity.Picking;
import com.packages.mapper.PickingMapper;
import com.packages.service.PickingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/picking")
public class PickingController extends BaseController<Picking, PickingService, PickingMapper> {
}
