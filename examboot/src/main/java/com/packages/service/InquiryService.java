package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.packages.entity.Inquiry;
import com.packages.mapper.InquiryMapper;
import com.packages.utils.QueryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Service
public class InquiryService{
    private final JdbcTemplate jdbcTemplate;
    private final InquiryMapper inquiryMapper;
    public InquiryService(JdbcTemplate jdbcTemplate, InquiryMapper inquiryMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.inquiryMapper = inquiryMapper;
    }

    public List<Inquiry> findAllInquiries(MultiValueMap<String, String> params) {
        QueryWrapper<Inquiry> queryWrapper = new QueryWrapper<>();

        // 根据参数构建查询条件
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String paramName = entry.getKey();
            List<String> paramValues = entry.getValue();
            queryWrapper.in(paramName, paramValues.toArray());
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

}
