package com.packages.controller;

import com.packages.service.MaterialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@RequestMapping("/material")
@RestController
public class MaterialController {
    @Resource
    private MaterialService materialService;
    @GetMapping("/getSearchCombination")
    public Map<String, List<Map<String, Object>>> getSearchCombination() {
        return materialService.getSearchCombination();
    }
}
