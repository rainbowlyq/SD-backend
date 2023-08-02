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
public class SalesOrderService {
    private final JdbcTemplate jdbcTemplate;
    private final SalesorderMapper salesorderMapper;
    private final SellMapper sellMapper;
    public SalesOrderService(JdbcTemplate jdbcTemplate, SalesorderMapper salesorderMapper,SellMapper sellMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.salesorderMapper = salesorderMapper;
        this.sellMapper=sellMapper;
    }

    public List<Salesorder> findAllSaleOrders(MultiValueMap<String, String> params) {
        QueryWrapper<Salesorder> queryWrapper = new QueryWrapper<>();

        // 根据参数构建查询条件
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String paramName = entry.getKey();
            List<String> paramValues = entry.getValue();
            if(paramValues.size()>0 && paramValues.get(0)!=""){
                queryWrapper.in(paramName, paramValues.toArray());}
        }
        return salesorderMapper.selectList(queryWrapper);
    }
    public Salesorder findSalesOrderByQuoid(String inqid) {
        QueryWrapper<Salesorder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("salordid", inqid);
        return salesorderMapper.selectOne(queryWrapper);
    }
    public int updateSalesOrder(Salesorder salesorder) {
        UpdateWrapper<Salesorder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("salordid", salesorder.getSalordid());
        return salesorderMapper.update(salesorder, updateWrapper);
    }
    public int insertSalesOrder(Salesorder salesorder) {
        salesorder.setCreatedate(DateFormat.getTimeNow());
        int rowsAffected = salesorderMapper.insert(salesorder);
        if (rowsAffected > 0) {
            return salesorder.getSalordid(); // 返回插入后生成的id
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }

    public int updateSell(List<Sell> sells) {
        int count=0;
        for (Sell sell : sells) {
            UpdateWrapper<Sell> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("salordid", sell.getSalordid());
            updateWrapper.eq("matid", sell.getMatid());
            count+=sellMapper.update(sell, updateWrapper);
        }
        return count;
    }
    public int insertSell(List<Sell> sells) {
        int count=0;
        for (Sell sell : sells) {
            count+=sellMapper.insert(sell);
        }
        return count;
    }

    public List<Map<String, Object>> findSalesOrderItemBySalordid(String salordid) {
        String sql = "SELECT e.*, m.* FROM salesorder i " +
                "INNER JOIN sell e ON i.salordid = e.salordid " +
                "INNER JOIN material_sd m ON e.matid = m.msdId " +
                "WHERE i.salordid = ?";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> result = jdbcTemplate.query(sql, new Object[]{salordid}, rowMapper);
        return result;
    }

    public Salesorder getBySalordId(Integer salordid) {
        return salesorderMapper.selectById(salordid);
    }
}
