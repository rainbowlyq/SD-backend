package com.packages.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.packages.entity.ContactPerson;
import com.packages.entity.Customer;
import com.packages.entity.MaterialSd;
import com.packages.mapper.ContactPersonMapper;
import com.packages.utils.QueryUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ContactPersonService {
    private final JdbcTemplate jdbcTemplate;
    private final ContactPersonMapper contactPersonMapper;
    public ContactPersonService(JdbcTemplate jdbcTemplate, ContactPersonMapper contactPersonMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.contactPersonMapper=contactPersonMapper;
    }

    public List<ContactPerson> search(Map<String, String> params) {
        QueryWrapper<ContactPerson> queryWrapper = new QueryWrapper<>();
        // 根据参数构建查询条件
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            if(paramValue!=" "){
                queryWrapper.eq(paramName, paramValue);
            }
        }

        return contactPersonMapper.selectList(queryWrapper);
    }
    public List<Map<String, Object>> searchByname(String name) {
        String sql = "SELECT * FROM `contact_person`  WHERE first_name LIKE ? OR last_name LIKE ?";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        String searchTerm = "%" + name + "%";
        List<Map<String, Object>> result = jdbcTemplate.query(sql, new Object[]{searchTerm, searchTerm}, rowMapper);
        return result;
    }

    public int insertcontactPerson(ContactPerson ContactPerson) {
        int rowsAffected = contactPersonMapper.insert(ContactPerson);
        if (rowsAffected > 0) {
            return ContactPerson.getPersonId(); // 返回插入后生成的id
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }

    public int updateContactPerson(ContactPerson ContactPerson) {
        int rowsAffected = contactPersonMapper.updateById(ContactPerson);
        if (rowsAffected > 0) {
            return 1; // 成功
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }
}
