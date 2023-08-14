package com.packages.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.packages.entity.Customer;
import com.packages.entity.Delivery;
import com.packages.entity.Picking;
import com.packages.entity.Salesorder;
import com.packages.mapper.CustomerMapper;
import com.packages.mapper.PickingMapper;
import com.packages.service.DeliveryService;
import com.packages.service.PickingService;
import com.packages.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/picking")
public class PickingController extends BaseController<Picking, PickingService, PickingMapper> {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private SalesOrderService salesOrderService;
    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/preview")
    public Map<String, Object> preview(@RequestBody Map<String, Object> data) {

        HashMap<String, Object> res = new HashMap<>();
        String sql = "SELECT e.*, m.*, (e.ordquantity - ifnull(picked.quantity, 0)) maxQuantity, ifnull(thelf_picked.quantity, 0) quantity " +
                "FROM salesorder i " +
                "         INNER JOIN sell e ON i.salordid = e.salordid " +
                "         INNER JOIN material_sd m ON e.matid = m.msdId " +
                "         left join (select ms.msdId, sum(p.quantity) quantity, d.salordid " +
                "                    from delivery d " +
                "                             inner join picking p on d.delid = p.delid and d.delid !=  " + data.get("delid") +
                "                             inner join material_sd ms on p.matid = ms.msdId " +
                "                    group by d.salordid, ms.msdId) picked on i.salordid = picked.salordid and m.msdId = picked.msdId " +
                "         left join (select sum(ip.quantity) quantity, ip.matid " +
                "                    from picking ip " +
                "                    where ip.delid =  " + data.get("delid") +
                "                    group by matid) thelf_picked on e.matid= thelf_picked.matid " +
                "where i.salordid = " + data.get("salordid");

        res.put("data", jdbcTemplate.queryForList(sql));
        Salesorder saleOrder = salesOrderService.getBySalordId(((Number) data.get("salordid")).intValue());

        Customer customer = customerMapper.selectOne(Wrappers.lambdaQuery(new Customer())
                .eq(Customer::getSrchterm, saleOrder.getCusref())
                .eq(Customer::getSaleOrg, saleOrder.getSorg())
                .eq(Customer::getDistrChannel, saleOrder.getDischannel())
                .eq(Customer::getDivision, saleOrder.getDivision())
        );

        Integer maxPartDeliveries = customer.getMaxPartDeliveries();

        if (maxPartDeliveries != null) {
            Delivery delivery = new Delivery();
            delivery.setStatus(null);
            if (maxPartDeliveries <= deliveryService.count(Wrappers.lambdaQuery(delivery)
                    .eq(Delivery::getSalordid, ((Number) data.get("salordid")).intValue()))
                    &&
                    deliveryService.count(Wrappers.lambdaQuery(delivery)
                            .eq(Delivery::getSalordid, ((Number) data.get("salordid")).intValue())
                            .lt(Delivery::getStatus, 3)) <= 1) {
                res.put("msg", "该发货单为该订单最后一次发货机会，请全部发货");
            }
        }

        return res;
    }

    public List<Map<String, Object>> preview0(Map<String, Object> data) {
        String sql = "SELECT e.*, m.*, (e.ordquantity - ifnull(picked.quantity, 0)) maxQuantity " +
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
                "where i.salordid = " + data.get("salordid");
        return jdbcTemplate.queryForList(sql);
    }

}
