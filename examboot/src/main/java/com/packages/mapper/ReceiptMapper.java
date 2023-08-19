package com.packages.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.packages.entity.Receipt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface ReceiptMapper extends BaseMapper<Receipt> {
    @Select("select r.*, s.salordid, s.currency, s.soldtoparty, c.name as soldtopartyName\n" +
            "from receipt r\n" +
            "         left join invoice i on r.invid = i.invid\n" +
            "         left join salesorder s on i.salordid = s.salordid\n" +
            "         left join customer c on s.soldtoparty = c.bp\n" +
            "where DATE(r.date) between #{startDate} and #{endDate}")
    List<Receipt> findAll(String startDate, String endDate);
}
