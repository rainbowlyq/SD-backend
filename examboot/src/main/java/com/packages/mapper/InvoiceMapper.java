package com.packages.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.packages.entity.Invoice;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {
}

