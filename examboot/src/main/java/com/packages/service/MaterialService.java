package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.packages.entity.Customer;
import com.packages.entity.MaterialSd;
import com.packages.entity.Relationship;
import com.packages.mapper.CustomerMapper;
import com.packages.mapper.MaterialMapper;
import com.packages.utils.QueryUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public int updateStorage(String time, @NotNull List<Map<String, String>> MI, int uid) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String timezone = "GMT+8";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        Date date = sdf.parse(time);
        for (Map<String, String> mi : MI){
            String material = mi.get("Material");
            int quantity = Integer.parseInt(mi.get("Quantity"));
            String plant = mi.get("Plant");
            String sLoc = mi.get("SLoc");
            String sql = "INSERT INTO confirmreceipt(uid,mid,plant,StorageLoc,amount,time) values(?,?,?,?,?,?)";
            int rowsAffected1 = jdbcTemplate.update(sql, uid,material, plant, sLoc, quantity, date);
            //如果SQL语句执行失败（例如语法错误或约束冲突等），返回值通常为0。
            if (rowsAffected1 == 0){
                return -1;
            }
            String sql1 = "SELECT Unrestricted FROM materialinventory WHERE Mid=? AND Plant=? AND StorageLoc=?";
            int oquantity = jdbcTemplate.queryForObject(sql1, Integer.class, material, plant, sLoc);
            int nquantity = oquantity + quantity;
            String sql2 = "UPDATE materialinventory\n" +
                    "SET Unrestricted = ?\n" +
                    "WHERE Mid=? AND Plant=? AND StorageLoc=?";
            int rowsAffected2 = jdbcTemplate.update(sql2,nquantity);
        }

        return 1;
    }

    public List<Map<String, String>> searchStorage(int mid, String plant){
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
        return ;
    }

}
