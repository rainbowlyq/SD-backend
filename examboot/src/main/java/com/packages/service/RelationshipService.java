package com.packages.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.packages.entity.Relationship;
import com.packages.mapper.RelationshipMapper;
import com.packages.utils.QueryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RelationshipService {
    private final JdbcTemplate jdbcTemplate;
    private final RelationshipMapper RelationshipMapper;
    public RelationshipService(JdbcTemplate jdbcTemplate, RelationshipMapper RelationshipMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.RelationshipMapper=RelationshipMapper;
    }

    //新建customer和contactPerson的关系
    public int insertRelationship(Relationship Relationship) {
        int rowsAffected = RelationshipMapper.insert(Relationship);
        if (rowsAffected > 0) {
            return 1; // 成功
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }
}
