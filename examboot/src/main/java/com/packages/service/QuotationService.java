package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.packages.entity.*;
import com.packages.mapper.DocconditionMapper;
import com.packages.mapper.ItemconditionMapper;
import com.packages.mapper.QuotateMapper;
import com.packages.mapper.QuotationMapper;
import com.packages.utils.DateFormat;
import com.packages.utils.QueryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuotationService{
    private final JdbcTemplate jdbcTemplate;
    private final QuotationMapper quotationMapper;
    private final QuotateMapper quotateMapper;
    private final ItemconditionMapper itemconditionMapper;
    private final DocconditionMapper docconditionMapper;
    public QuotationService(JdbcTemplate jdbcTemplate, QuotationMapper quotationMapper,QuotateMapper quotateMapper,ItemconditionMapper itemconditionMapper,DocconditionMapper docconditionMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.quotationMapper = quotationMapper;
        this.quotateMapper=quotateMapper;
        this.itemconditionMapper=itemconditionMapper;
        this.docconditionMapper=docconditionMapper;
    }

    public List<Quotation> findAllQuotations(MultiValueMap<String, String> params) {
        QueryWrapper<Quotation> queryWrapper = new QueryWrapper<>();

        // 根据参数构建查询条件
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String paramName = entry.getKey();
            List<String> paramValues = entry.getValue();
            if(paramValues.size()>0 && paramValues.get(0)!=""){
                queryWrapper.in(paramName, paramValues.toArray());}
        }
        return quotationMapper.selectList(queryWrapper);
    }
    public Quotation findQuotationByQuoid(String inqid) {
        QueryWrapper<Quotation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("quoid", inqid);
        return quotationMapper.selectOne(queryWrapper);
    }
    public int updateQuotation(Quotation quotation) {
        UpdateWrapper<Quotation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("quoid", quotation.getQuoid());
        return quotationMapper.update(quotation, updateWrapper);
    }
    public int insertQuotation(Quotation quotation) {
        quotation.setCreatedate(DateFormat.getTimeNow());
        int rowsAffected = quotationMapper.insert(quotation);
        if (rowsAffected > 0) {
            return quotation.getQuoid(); // 返回插入后生成的id
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }

    public int updateQuotate(List<Quotate> quotates) {
        int count=0;
        for (Quotate quotate : quotates) {
            UpdateWrapper<Quotate> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("quoid", quotate.getQuoid());
            updateWrapper.eq("matid", quotate.getMatid());
            count+=quotateMapper.update(quotate, updateWrapper);
        }
        return count;
    }
    public int insertQuotate(List<Quotate> quotates) {
        int count=0;
        for (Quotate quotate : quotates) {
            count+=quotateMapper.insert(quotate);
        }
        return count;
    }

    public List<Map<String, Object>> testJDBC(String quoid) {
        String sql = "SELECT i.*, e.* FROM quotation i " +
                "LEFT JOIN quotate e ON i.quoid = e.quoid " +
                "WHERE i.quoid = ?";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        return jdbcTemplate.query(sql, new Object[] { quoid }, rowMapper);
    }

    public List<Map<String, Object>> findQuotationItemByQuoid(String quoid) {
        String sql = "SELECT e.*, m.* FROM quotation i " +
                "INNER JOIN quotate e ON i.quoid = e.quoid " +
                "INNER JOIN material_sd m ON e.matid = m.msdId " +
                "WHERE i.quoid = ?";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> result = jdbcTemplate.query(sql, new Object[]{quoid}, rowMapper);
        return result;
    }
    public int insertItemCondition(List<Itemcondition> itemconditions) {
        int count=0;
        for (Itemcondition itemcondition : itemconditions) {
            count+=itemconditionMapper.insert(itemcondition);
        }
        return count;
    }
    public List<Itemcondition> findItemCondition(String docid) {
        QueryWrapper<Itemcondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("docid", docid);
        return itemconditionMapper.selectList(queryWrapper);
    }
    public int updateItemCondition(List<Itemcondition> itemconditions) {
        int count=0;
        for (Itemcondition itemcondition : itemconditions) {
            UpdateWrapper<Itemcondition> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("docid", itemcondition.getDocid());
            updateWrapper.eq("matid", itemcondition.getMatid());
            updateWrapper.eq("contype", itemcondition.getContype());
            count+=itemconditionMapper.update(itemcondition, updateWrapper);
        }
        return count;
    }
    public int insertDocCondition(List<Doccondition> docconditions) {
        int count=0;
        for (Doccondition doccondition : docconditions) {
            count+=docconditionMapper.insert(doccondition);
        }
        return count;
    }
    public List<Doccondition> findDocCondition(String docid) {
        QueryWrapper<Doccondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("docid", docid);
        return docconditionMapper.selectList(queryWrapper);
    }
    public int updateDocCondition(List<Doccondition> docconditions) {
        int count=0;
        for (Doccondition doccondition : docconditions) {
            UpdateWrapper<Doccondition> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("docid", doccondition.getDocid());
            updateWrapper.eq("contype", doccondition.getContype());
            count+=docconditionMapper.update(doccondition, updateWrapper);
        }
        return count;
    }
    public int setIsRefed(String quoid) {
        Quotation quotation = new Quotation();
        quotation.setIsrefed(1); // 设置 refer 列的值为 1
        UpdateWrapper<Quotation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("quoid", quoid); // 设置更新条件为 id = 指定值
        return quotationMapper.update(quotation, updateWrapper);
    }
    public Map<String, List<Map<String, Object>>> getSearchCombination(){
        String sql1 = "SELECT DISTINCT sorg FROM quotation" ;
        String sql2 = "SELECT DISTINCT dischannel FROM quotation" ;
        String sql3 = "SELECT DISTINCT division FROM quotation" ;
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> sorg =jdbcTemplate.query(sql1, rowMapper);
        List<Map<String, Object>> dischannel= jdbcTemplate.query(sql2, rowMapper);
        List<Map<String, Object>> division= jdbcTemplate.query(sql3, rowMapper);
        Map<String, List<Map<String, Object>>> combinationMap = new HashMap<>();
        combinationMap.put("sorg",sorg);
        combinationMap.put("dischannel", dischannel);
        combinationMap.put("division", division);
        return combinationMap;
    }
}
