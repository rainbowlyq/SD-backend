package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.packages.entity.Inquire;
import com.packages.entity.Quotate;
import com.packages.entity.Inquiry;
import com.packages.entity.Quotation;
import com.packages.mapper.InquireMapper;
import com.packages.mapper.InquiryMapper;
import com.packages.mapper.QuotateMapper;
import com.packages.mapper.QuotationMapper;
import com.packages.utils.QueryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Service
public class QuotationService{
    private final JdbcTemplate jdbcTemplate;
    private final QuotationMapper quotationMapper;
    private final QuotateMapper quotateMapper;
    public QuotationService(JdbcTemplate jdbcTemplate, QuotationMapper quotationMapper,QuotateMapper quotateMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.quotationMapper = quotationMapper;
        this.quotateMapper=quotateMapper;
    }

    public List<Quotation> findAllQuotations(MultiValueMap<String, String> params) {
        QueryWrapper<Quotation> queryWrapper = new QueryWrapper<>();

        // 根据参数构建查询条件
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String paramName = entry.getKey();
            List<String> paramValues = entry.getValue();
            queryWrapper.in(paramName, paramValues.toArray());
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

}
