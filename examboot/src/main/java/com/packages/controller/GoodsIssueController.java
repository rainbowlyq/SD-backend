package com.packages.controller;

import com.packages.entity.GoodsIssue;
import com.packages.mapper.GoodsIssueMapper;
import com.packages.service.GoodsIssueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods/issue")
public class GoodsIssueController extends BaseController<GoodsIssue, GoodsIssueService, GoodsIssueMapper> {
}
