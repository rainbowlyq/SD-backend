package com.packages.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.packages.entity.ContactPerson;
import com.packages.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContactPersonMapper extends BaseMapper<ContactPerson> {


}

