package com.packages.service;

import com.packages.entity.Delivery;
import com.packages.entity.MaterialSd;
import com.packages.entity.Picking;
import com.packages.entity.Sell;
import com.packages.mapper.MaterialMapper;
import com.packages.mapper.PickingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PickingService extends BaseService<PickingMapper, Picking> {
    @Autowired
    private SellService sellService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private SalesOrderService salesOrderService;
    @Autowired
    private MaterialMapper materialMapper;
    
    public Picking updateProperties(Picking picking) {
        Delivery delivery = deliveryService.getById(picking.getDelid());
        Sell sell = new Sell();
        sell.setSalordid(delivery.getSalordid());
        sell.setMatid(picking.getMatid());
        sell = sellService.search(sell).get(0);
        if(sell.getConditonprice()!=null){
            picking.setConditionPrice(sell.getConditonprice());
        }
        picking.setCurrency(salesOrderService.getBySalordId(sell.getSalordid()).getCurrency());
        MaterialSd material = materialMapper.selectById(picking.getMatid());
        if (material != null) {
            picking.setWeight(material.getWeight());
            picking.setWeightUnit(material.getWeightunit());
        }
        return picking;
    }
}
