package com.packages.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.packages.entity.Delivery;
import com.packages.entity.DeliveryItem;
import com.packages.entity.Picking;
import com.packages.entity.Sell;
import com.packages.mapper.DeliveryMapper;
import com.packages.service.DeliveryItemService;
import com.packages.service.DeliveryService;
import com.packages.service.PickingService;
import com.packages.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
@RequestMapping("/delivery")
public class DeliveryController extends BaseController<Delivery, DeliveryService, DeliveryMapper> {


    @Autowired
    private DeliveryItemService deliveryItemService;

    @Autowired
    private PickingService pickingService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SalesOrderService salesOrderService;

    @PostMapping("/pick")
    public String pick(@RequestBody Delivery delivery) {
        Long delid = delivery.getDelid();
        ArrayList<DeliveryItem> deliveryItems = new ArrayList<>();
        ArrayList<Picking> pickings = new ArrayList<>();
        LocalDate pickingDate = delivery.getPickingDate();
        if (pickingDate == null) {
            pickingDate = LocalDate.now();
        }

        for (Sell item : delivery.getItems()) {
            Integer ordquantity = item.getOrdquantity();
            if (ordquantity == null || ordquantity < 0) {
                continue;
            }
            DeliveryItem deliveryItem = new DeliveryItem();
            deliveryItem.setDelid(delid);
            deliveryItem.setMatid(item.getMsdid());
            deliveryItem.setQuantity(ordquantity);
            deliveryItem.setAvgvalue(item.getPrice());
            deliveryItems.add(deliveryItem);


            Picking picking = new Picking();
            picking.setDelid(delid);
            picking.setMatid(item.getMatid());
            picking.setQuantity(ordquantity);
            picking.setDate(pickingDate);
            picking.setPlant(item.getDelstorplant());
            picking.setStorageloc(item.getStorageloc());
            pickings.add(picking);
        }
        if (CollectionUtils.isEmpty(pickings)) {
            return "未选择分拣物料";
        }

        jdbcTemplate.update("update materialinventory mi inner join (select ms.mid, ms.delstorplant, ms.storageloc, ifnull(sum(p.quantity), 0) quantity " +
                "                                        from picking p " +
                "                                                 inner join material_sd ms on p.matid = ms.msdId and p.delid =  " + delid +
                "                                       group by ms.mid, ms.delstorplant, ms.storageloc) picked on mi.Mid = picked.mid and mi.Plant = picked.delstorplant and mi.StorageLoc = picked.storageloc " +
                "set mi.SchedForDel = mi.SchedForDel - picked.quantity");

        jdbcTemplate.update("update materialinventory mi inner join (select ms.mid, ms.delstorplant, ms.storageloc, ifnull(sum(p.quantity), 0) quantity " +
                "                                        from picking p " +
                "                                                 inner join material_sd ms on p.matid = ms.msdId and p.delid =  " + delid +
                "                                       group by ms.mid, ms.delstorplant, ms.storageloc) picked on mi.Mid = picked.mid and mi.Plant = picked.delstorplant and mi.StorageLoc = picked.storageloc " +
                "set mi.SalesOrder = mi.SalesOrder + picked.quantity");

        deliveryItemService.remove(Wrappers.lambdaQuery(new DeliveryItem()).eq(DeliveryItem::getDelid, delid));
        deliveryItemService.saveBatch(deliveryItems);
        pickingService.remove(Wrappers.lambdaQuery(new Picking()).eq(Picking::getDelid, delid));
        pickingService.saveBatch(pickings);
        jdbcTemplate.update("update materialinventory mi inner join (select ms.mid, ms.delstorplant, ms.storageloc, ifnull(sum(p.quantity), 0) quantity " +
                "                                        from picking p " +
                "                                                 inner join material_sd ms on p.matid = ms.msdId and p.delid =  " + delid +
                "                                       group by ms.mid, ms.delstorplant, ms.storageloc) picked on mi.Mid = picked.mid and mi.Plant = picked.delstorplant and mi.StorageLoc = picked.storageloc " +
                "set mi.SchedForDel = mi.SchedForDel + picked.quantity");

        jdbcTemplate.update("update materialinventory mi inner join (select ms.mid, ms.delstorplant, ms.storageloc, ifnull(sum(p.quantity), 0) quantity " +
                "                                        from picking p " +
                "                                                 inner join material_sd ms on p.matid = ms.msdId and p.delid =  " + delid +
                "                                       group by ms.mid, ms.delstorplant, ms.storageloc) picked on mi.Mid = picked.mid and mi.Plant = picked.delstorplant and mi.StorageLoc = picked.storageloc " +
                "set mi.SalesOrder = mi.SalesOrder - picked.quantity");
        delivery.setStatus(2);
        service.updateById(delivery);


        salesOrderService.updateSalesOrderStatus(salesOrderService.getById(service.getById(delivery.getDelid()).getSalordid()));
        return null;
    }


    @PostMapping("/updateStatus")
    public void updateStatus(@RequestBody Delivery delivery) {
        Delivery d = new Delivery();
        d.setStatus(null);
        service.update(Wrappers.lambdaUpdate(d)
                .eq(Delivery::getDelid, delivery.getDelid())
                .set(Delivery::getStatus, delivery.getStatus())
        );

        salesOrderService.updateSalesOrderStatus(salesOrderService.getById(service.getById(delivery.getDelid()).getSalordid()));

    }
}
