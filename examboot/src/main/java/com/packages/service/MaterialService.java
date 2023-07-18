package com.packages.service;

import com.packages.mapper.CustomerMapper;
import com.packages.mapper.MaterialMapper;
import com.packages.utils.QueryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaterialService {
    private final JdbcTemplate jdbcTemplate;
    private final MaterialMapper materialMapper;
    public MaterialService(JdbcTemplate jdbcTemplate,MaterialMapper materialMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.materialMapper= materialMapper;
    }
    public Map<String, List<Map<String, Object>>> getSearchCombination(){
        String sql1 = "SELECT DISTINCT country FROM customer" ;
        String sql2 = "SELECT DISTINCT region FROM customer" ;
        String sql3 = "SELECT DISTINCT city FROM customer" ;
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> country= jdbcTemplate.query(sql1, rowMapper);
        List<Map<String, Object>> region= jdbcTemplate.query(sql2, rowMapper);
        List<Map<String, Object>> city= jdbcTemplate.query(sql3, rowMapper);
        Map<String, List<Map<String, Object>>> combinationMap = new HashMap<>();
        combinationMap.put("country", country);
        combinationMap.put("region", region);
        combinationMap.put("city", city);
        return combinationMap;
    }
}
