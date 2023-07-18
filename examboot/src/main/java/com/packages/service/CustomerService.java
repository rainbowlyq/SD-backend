package com.packages.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.packages.entity.Customer;
import com.packages.entity.Inquiry;
import com.packages.mapper.CustomerMapper;
import com.packages.mapper.InquiryMapper;
import com.packages.utils.QueryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService{
    private final JdbcTemplate jdbcTemplate;
    private final CustomerMapper customerMapper;
    public CustomerService(JdbcTemplate jdbcTemplate,CustomerMapper customerMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerMapper=customerMapper;
    }
    public List<Map<String, Object>> divisionCombination() {
        String sql = "SELECT DISTINCT sales_org, distr_channel, division\n" +
                "FROM sale_area\n" +
                "GROUP BY sales_org, distr_channel, division;";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        return jdbcTemplate.query(sql, rowMapper);
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
    public List<Customer> findAllCustomers(Map<String, String> params) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();

        // 根据参数构建查询条件
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            if(paramValue!=""){
                queryWrapper.eq(paramName, paramValue);
            }
        }
        return customerMapper.selectList(queryWrapper);
    }

}
