package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.packages.entity.Inquire;
import com.packages.entity.Inquiry;
import com.packages.mapper.InquireMapper;
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
public class InquiryService{
    private final JdbcTemplate jdbcTemplate;
    private final InquiryMapper inquiryMapper;
    private final InquireMapper inquireMapper;
    public InquiryService(JdbcTemplate jdbcTemplate, InquiryMapper inquiryMapper,InquireMapper inquireMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.inquiryMapper = inquiryMapper;
        this.inquireMapper=inquireMapper;
    }

    public List<Inquiry> findAllInquiries(MultiValueMap<String, String> params) {
        QueryWrapper<Inquiry> queryWrapper = new QueryWrapper<>();

        // 根据参数构建查询条件
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String paramName = entry.getKey();
            List<String> paramValues = entry.getValue();
            if(paramValues.size()>0 && paramValues.get(0)!=""){
            queryWrapper.in(paramName, paramValues.toArray());}
        }
        return inquiryMapper.selectList(queryWrapper);
    }
    public Inquiry findInquiryByInqid(String inqid) {
        QueryWrapper<Inquiry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inqid", inqid);
        return inquiryMapper.selectOne(queryWrapper);
    }
    public int updateInquiry(Inquiry inquiry) {
        UpdateWrapper<Inquiry> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("inqid", inquiry.getInqid());
        return inquiryMapper.update(inquiry, updateWrapper);
    }
    public int insertInquiry(Inquiry inquiry) {
        int rowsAffected = inquiryMapper.insert(inquiry);
        if (rowsAffected > 0) {
            return inquiry.getInqid(); // 返回插入后生成的id
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }

    public int updateInquire(List<Inquire> inquires) {
        int count=0;
        for (Inquire inquire : inquires) {
            UpdateWrapper<Inquire> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("inqid", inquire.getInqid());
            updateWrapper.eq("matid", inquire.getMatid());
            count+=inquireMapper.update(inquire, updateWrapper);
        }
        return count;
    }
    public int insertInquire(List<Inquire> inquires) {
        int count=0;
        for (Inquire inquire : inquires) {
            count+=inquireMapper.insert(inquire);
        }
        return count;
    }

    public List<Map<String, Object>> testJDBC(String inqid) {
        String sql = "SELECT i.*, e.* FROM inquiry i " +
                "LEFT JOIN inquire e ON i.inqid = e.inqid " +
                "WHERE i.inqid = ?";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        return jdbcTemplate.query(sql, new Object[] { inqid }, rowMapper);
    }

    public List<Map<String, Object>> findInquiryItemByInqid(String inqid) {
        String sql = "SELECT e.*, m.* FROM inquiry i " +
                "INNER JOIN inquire e ON i.inqid = e.inqid " +
                "INNER JOIN material_sd m ON e.matid = m.msdId " +
                "WHERE i.inqid = ?";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> result = jdbcTemplate.query(sql, new Object[]{inqid}, rowMapper);
        return result;
    }
    public int setIsRefed(String inqid) {
        Inquiry inquiry = new Inquiry();
        inquiry.setIsrefed(1); // 设置 refer 列的值为 1
        UpdateWrapper<Inquiry> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("inqid", inqid); // 设置更新条件为 id = 指定值
        return inquiryMapper.update(inquiry, updateWrapper);
    }
    public Map<String, List<Map<String, Object>>> getSearchCombination(){
        String sql1 = "SELECT DISTINCT sorg FROM inquiry" ;
        String sql2 = "SELECT DISTINCT dischannel FROM inquiry" ;
        String sql3 = "SELECT DISTINCT division FROM inquiry" ;
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
