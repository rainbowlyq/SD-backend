package com.packages.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.packages.entity.Delivery;
import com.packages.entity.DeliveryItem;
import com.packages.entity.Picking;
import com.packages.entity.Sell;
import com.packages.mapper.DeliveryMapper;
import com.packages.service.DeliveryItemService;
import com.packages.service.DeliveryService;
import com.packages.service.PickingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    @PostMapping("/pick")
    @Transactional
    public void pick(@RequestBody Delivery delivery) {
        ArrayList<DeliveryItem> deliveryItems = new ArrayList<>();
        ArrayList<Picking> pickings = new ArrayList<>();
        LocalDate pickingDate = delivery.getPickingDate();
        if (pickingDate == null) {
            pickingDate = LocalDate.now();
        }
        for (Sell item : delivery.getItems()) {
            DeliveryItem deliveryItem = new DeliveryItem();
            deliveryItem.setDelid(delivery.getDelid());
            deliveryItem.setMatid(item.getMsdid());
            deliveryItem.setQuantity(item.getOrdquantity());
            deliveryItem.setAvgvalue(item.getPrice());
            deliveryItems.add(deliveryItem);

            Picking picking = new Picking();
            picking.setDelid(delivery.getDelid());
            picking.setMatid(item.getMsdid());
            picking.setQuantity(item.getOrdquantity());
            picking.setDate(pickingDate);
            picking.setPlant(item.getDelstorplant());
            picking.setStorageloc(item.getStorageloc());
            pickings.add(picking);
        }
        deliveryItemService.remove(Wrappers.lambdaQuery(new DeliveryItem()).eq(DeliveryItem::getDelid, delivery.getDelid()));
        deliveryItemService.saveBatch(deliveryItems);
        pickingService.remove(Wrappers.lambdaQuery(new Picking()).eq(Picking::getDelid, delivery.getDelid()));
        pickingService.saveBatch(pickings);
        delivery.setStatus(2);
        service.updateById(delivery);

    }


    @PostMapping("/updateStatus")
    @Transactional
    public void updateStatus(@RequestBody Delivery delivery) {
        Delivery d = new Delivery();
        d.setStatus(null);
        service.update(Wrappers.lambdaUpdate(d)
                .eq(Delivery::getDelid, delivery.getDelid())
                .set(Delivery::getStatus, delivery.getStatus())
        );
    }
}
