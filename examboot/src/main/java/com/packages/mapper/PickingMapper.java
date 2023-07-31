package com.packages.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.packages.entity.Picking;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PickingMapper extends BaseMapper<Picking> {
}
