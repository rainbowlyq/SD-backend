package com.packages.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.packages.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface CustomerService extends BaseMapper<Customer> {
}
