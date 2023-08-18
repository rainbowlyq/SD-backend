package com.packages.service;

import com.packages.entity.Delivery;
import com.packages.entity.Picking;
import com.packages.entity.Sell;
import com.packages.mapper.PickingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PickingService extends BaseService<PickingMapper, Picking> {
    @Autowired
    private SellService sellService;
    @Autowired
    private DeliveryService deliveryService;
    
    public Picking updateConditionPrice(Picking picking) {
        Delivery delivery = deliveryService.getById(picking.getDelid());
        Sell sell = new Sell();
        sell.setSalordid(delivery.getSalordid());
        //todo
        sell = sellService.search(sell).get(0);
        picking.setConditionPrice(sell.getConditonprice());
        return picking;
    }
}
