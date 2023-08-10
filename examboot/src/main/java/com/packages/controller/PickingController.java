package com.packages.controller;

import com.packages.entity.Picking;
import com.packages.mapper.PickingMapper;
import com.packages.service.PickingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/picking")
public class PickingController extends BaseController<Picking, PickingService, PickingMapper> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("/preview")
    public List<Map<String, Object>> preview(@RequestBody Map<String, Object> data) {

        return jdbcTemplate.queryForList("SELECT e.*, m.*, (e.ordquantity - ifnull(picked.quantity, 0)) maxQuantity " +
                "FROM salesorder i " +
                "         INNER JOIN sell e ON i.salordid = e.salordid " +
                "         INNER JOIN material_sd m ON e.matid = m.msdId " +
                "         left join (select ms.msdId, sum(p.quantity) quantity, d.salordid " +
                "                    from delivery d " +
                "                             inner join picking p on d.delid = p.delid " +
                "                             inner join material_sd ms " +
                "                                        on p.matid = ms.msdid " +
                " " +
                "                    group by d.salordid, ms.msdId) picked " +
                "                   on i.salordid = picked.salordid and m.msdId = picked.msdId " +
                " " +
                "where i.salordid = " + data.get("salordid"));
    }
}
