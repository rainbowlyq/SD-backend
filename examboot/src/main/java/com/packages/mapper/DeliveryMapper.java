package com.packages.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.packages.entity.Delivery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeliveryMapper extends BaseMapper<Delivery> {
    @Select({
            "<script>",
            "select d.*, p.date pickingDate ",
            "from delivery d ",
            "left join (select p_1.delid, min(p_1.date) date from picking p_1 group by p_1.delid) p on d.delid = p.delid ",
            "</script>"
    })
    List<Delivery> search(Delivery delivery);
}
