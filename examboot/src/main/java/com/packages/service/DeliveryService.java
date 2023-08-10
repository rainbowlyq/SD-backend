package com.packages.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.packages.controller.PickingController;
import com.packages.entity.Delivery;
import com.packages.entity.DeliveryItem;
import com.packages.entity.GoodsIssue;
import com.packages.entity.Salesorder;
import com.packages.mapper.DeliveryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeliveryService extends BaseService<DeliveryMapper, Delivery> {

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private DeliveryItemService deliveryItemService;

    @Autowired
    private GoodsIssueService giService;


    @Autowired
    private PickingController pickingController;

    @Override
    public List<Delivery> search(Delivery delivery) {
        return getBaseMapper().search(delivery);
    }

    @Override
    @Transactional
    public String createOrUpdate(Delivery delivery) {
        Integer salordid = delivery.getSalordid();
        Salesorder saleOrder = salesOrderService.getBySalordId(salordid);
        if (saleOrder == null) {
            return "该订单不存在，无法创建发货单";

        }
        HashMap<String, Object> pickingPreview = new HashMap<>();
        pickingPreview.put("salordid", salordid);
        List<Map<String, Object>> preview = pickingController.preview(pickingPreview);
        for (Map<String, Object> p : preview) {
            Number maxQuantity = (Number) p.get("maxQuantity");
            if (maxQuantity != null && maxQuantity.longValue() > 0) {

                delivery.setShiptoparty(saleOrder.getShiptoparty());
                if (delivery.getDelid() == null) {
                    save(delivery);
                } else {
                    updateById(delivery);
                }

                return null;
            }
        }

        return "该订单已全部分拣，无法创建发货单";
    }

    @Override
    @Transactional
    public void deleteById(Delivery delivery) {
        Long delid = delivery.getDelid();
        if (delid == null) {
            return;
        }

        removeById(delid);
        DeliveryItem deleteDeliveryItem = new DeliveryItem();
        deleteDeliveryItem.setDelid(delid);
        deliveryItemService.remove(Wrappers.query(deleteDeliveryItem));
        GoodsIssue deleteGi = new GoodsIssue();
        deleteGi.setDelid(delid);
        giService.remove(Wrappers.query(deleteGi));
    }
    
    public List<Delivery> findAllBySalOrdId(int salordid){
        return getBaseMapper().findAllBySalOrdId(salordid);
    }
}
