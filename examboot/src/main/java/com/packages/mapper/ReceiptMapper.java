package com.packages.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.packages.entity.Receipt;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ReceiptMapper extends BaseMapper<Receipt> {
}
