package com.packages.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.packages.entity.Quotation;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface QuotationMapper extends BaseMapper<Quotation> {

}
