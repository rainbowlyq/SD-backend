package com.packages.controller;

import com.packages.entity.Relationship;
import com.packages.service.ContactPersonService;
import com.packages.service.CustomerService;
import com.packages.service.RelationshipService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping("/relationship")
@RestController
public class RelationshipCotroller {
    @Resource
    private RelationshipService RelationshipService;

    @PostMapping("/insert")
    public int insert(@RequestBody Relationship Relationship) {
        return RelationshipService.insertRelationship(Relationship);
    }
}
