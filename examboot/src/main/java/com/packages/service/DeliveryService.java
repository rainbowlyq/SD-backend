package com.packages.service;

import com.packages.entity.Delivery;
import com.packages.entity.Salesorder;
import com.packages.entity.Sell;
import com.packages.mapper.DeliveryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class DeliveryService extends BaseService<DeliveryMapper, Delivery> {

    @Autowired
    private SalesOrderService salesOrderService;

    @Override
    public List<Delivery> search(Delivery delivery) {
        return getBaseMapper().search(delivery);
    }

    @Override
    @Transactional
    public boolean createOrUpdate(Delivery delivery) {
        List<Sell> items = delivery.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return false;
        }

        Integer salordid = delivery.getSalordid();
        Salesorder saleOrder = salesOrderService.getBySalordId(salordid);
        delivery.setShiptoparty(saleOrder.getShiptoparty());
        if (delivery.getDelid() == null) {
            save(delivery);
        } else {
            updateById(delivery);
        }

        return true;
    }

    @Override
    @Transactional
    public void deleteById(Delivery delivery) {
        Long delid = delivery.getDelid();
        if (delid == null) {
            return;
        }

        removeById(delid);
    }
}
