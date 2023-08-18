package com.packages.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.packages.entity.Delivery;
import com.packages.entity.GoodsIssue;
import com.packages.entity.Picking;
import com.packages.mapper.GoodsIssueMapper;
import com.packages.service.DeliveryService;
import com.packages.service.GoodsIssueService;
import com.packages.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods/issue")
public class GoodsIssueController extends BaseController<GoodsIssue, GoodsIssueService, GoodsIssueMapper> {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private SalesOrderService salesOrderService;

    @PostMapping("/preview")
    public GoodsIssue preview(@RequestBody GoodsIssue gi) {
        GoodsIssue res = service.getOne(Wrappers.lambdaQuery(new GoodsIssue()), false);
        if (res == null) {
            res = new GoodsIssue();
            res.setDelid(gi.getDelid());
            res.setDate(LocalDate.now());
        }
        Delivery delivery = deliveryService.getById(gi.getDelid());
        Picking dp = new Picking();
        dp.setDelid(delivery.getDelid());
        Map<String, Object> order = jdbcTemplate.queryForMap("select s.* " +
                "from delivery d " +
                "         inner join salesorder s on d.salordid = s.salordid " +
                "where d.delid = " + delivery.getDelid());


        List<Map<String, Object>> items = jdbcTemplate.queryForList("select ms.*,p.quantity " +
                "from delivery d " +
                "         inner join picking p on d.delid = p.delid " +
                "         inner join material_sd ms " +
                "                    on p.matid = ms.msdid " +
                "where d.delid = " + delivery.getDelid());

        res.setOrder(order);
        res.setItems(items);
        return res;
    }

    @PostMapping("/save")
    public void save(@RequestBody GoodsIssue gi) {
        gi.setGiid(null);
        service.save(gi);
        Delivery delivery = new Delivery();
        delivery.setStatus(null);
        deliveryService.update(Wrappers.lambdaUpdate(delivery)
                .eq(Delivery::getDelid, gi.getDelid())
                .set(Delivery::getStatus, 3)

        );

        salesOrderService.updateSalesOrderStatus(salesOrderService.getById(deliveryService.getById(gi.getDelid()).getSalordid()));


        String sql = "update materialinventory mi inner join (select ms.mid, ms.storageloc, ms.delstorplant, sum(p.quantity) quantity" +
                "                                        from picking p" +
                "                                                 inner join material_sd ms on ms.msdId = p.matid" +
                "                                        where p.delid = " + gi.getDelid() +
                "                                        group by ms.mid, ms.storageloc, ms.delstorplant) m on mi.Mid = m.mid and" +
                "                                                                                              m.storageloc =" +
                "                                                                                              mi.StorageLoc and" +
                "                                                                                              m.delstorplant =" +
                "                                                                                              mi.StorageLoc " +
                "set mi.SchedForDel = mi.SchedForDel - m.quantity";
        jdbcTemplate.update(sql);
    }
}
