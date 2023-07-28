package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.packages.entity.Customer;
import com.packages.entity.MaterialSd;
import com.packages.entity.Relationship;
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
        String sql1 = "SELECT DISTINCT bomUnit bomUnit FROM material_sd" ;
        String sql2 = "SELECT DISTINCT weightUnit weightUnit FROM material_sd" ;
        String sql3 = "SELECT DISTINCT delStorPlant delStorPlant FROM material_sd" ;
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> bom_unit =jdbcTemplate.query(sql1, rowMapper);
        List<Map<String, Object>> weight_unit= jdbcTemplate.query(sql2, rowMapper);
        List<Map<String, Object>> del_stor_plant= jdbcTemplate.query(sql3, rowMapper);
        Map<String, List<Map<String, Object>>> combinationMap = new HashMap<>();
        combinationMap.put("bomUnit", bom_unit);
        combinationMap.put("weightUnit", weight_unit);
        combinationMap.put("delStorPlant", del_stor_plant);
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

    public int insertMaterials(MaterialSd MaterialSd) {
        int rowsAffected = materialMapper.insert(MaterialSd);
        if (rowsAffected > 0) {
            return 1; // 成功
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }

    public int updateMaterials(MaterialSd MaterialSd) {
        int rowsAffected = materialMapper.updateById(MaterialSd);
        if (rowsAffected > 0) {
            return 1; // 成功
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }

//    time: '',
//tableData: [
//{Material: '', Quantity: null, Plant: '', SLoc: ''},
//],
    public int updateStorage(String time,List<Map<String, String>> MI) {
        for (Map<String, String> mi : MI){
            String material = mi.get("Material");

        }
    }

}
