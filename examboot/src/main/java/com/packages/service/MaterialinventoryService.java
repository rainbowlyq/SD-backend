package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.packages.entity.Salesorder;
import com.packages.entity.Sell;
import com.packages.mapper.SalesorderMapper;
import com.packages.mapper.SellMapper;
import com.packages.utils.DateFormat;
import com.packages.utils.QueryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Service
public class MaterialinventoryService {
    private final JdbcTemplate jdbcTemplate;

    public MaterialinventoryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
