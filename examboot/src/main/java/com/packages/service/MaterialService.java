package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.packages.entity.Customer;
import com.packages.entity.MaterialSd;
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
        String sql1 = "SELECT DISTINCT bom_unit FROM material_sd" ;
        String sql2 = "SELECT DISTINCT weight_unit FROM material_sd" ;
        String sql3 = "SELECT DISTINCT del_stor_plant FROM material_sd" ;
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> bom_unit =jdbcTemplate.query(sql1, rowMapper);
        List<Map<String, Object>> weight_unit= jdbcTemplate.query(sql2, rowMapper);
        List<Map<String, Object>> del_stor_plant= jdbcTemplate.query(sql3, rowMapper);
        Map<String, List<Map<String, Object>>> combinationMap = new HashMap<>();
        combinationMap.put("bom_unit", bom_unit);
        combinationMap.put("weight_unit", weight_unit);
        combinationMap.put("del_stor_plant", del_stor_plant);
        return combinationMap;
    }
    public List<MaterialSd> findAllMaterials(Map<String, String> params) {
        QueryWrapper<MaterialSd> queryWrapper = new QueryWrapper<>();
        // 根据参数构建查询条件
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            if(paramValue!=""){
                queryWrapper.eq(paramName, paramValue);
            }
        }
        return materialMapper.selectList(queryWrapper);
    }
}
