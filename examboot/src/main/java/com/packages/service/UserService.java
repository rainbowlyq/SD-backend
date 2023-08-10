package com.packages.service;

import com.packages.entity.User;
import com.packages.mapper.MaterialMapper;
import com.packages.mapper.UserMapper;
import com.packages.utils.QueryUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper UserMapper;

    public UserService(JdbcTemplate jdbcTemplate, UserMapper UserMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.UserMapper = UserMapper;
    }

    public User login(String uname,String pwd,int client) {
        String sql1 = "SELECT * FROM user WHERE uname=?  AND pwd=? AND client=?";
        User User = null;
        try {
            User = jdbcTemplate.queryForObject(sql1, User.class, uname, pwd,client);
        } catch (EmptyResultDataAccessException e) {
            // 查询结果为null，处理异常情况
            // 在这里进行适当的处理
            return User;
        }
        return User;
    }
}
