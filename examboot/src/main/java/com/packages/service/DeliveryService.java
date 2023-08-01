package com.packages.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.packages.entity.Delivery;
import com.packages.entity.DeliveryItem;
import com.packages.entity.Sell;
import com.packages.mapper.DeliveryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryService extends BaseService<DeliveryMapper, Delivery> {

    @Autowired
    private DeliveryItemService itemService;

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


        if (delivery.getDelid() == null) {
            save(delivery);
        } else {
            updateById(delivery);
        }


        ArrayList<DeliveryItem> deliveryItems = new ArrayList<>();
        for (Sell item : items) {
            DeliveryItem deliveryItem = new DeliveryItem();
            deliveryItem.setDelid(delivery.getDelid());
            deliveryItem.setMatid(item.getMatid());
            deliveryItem.setQuantity(item.getOrdquantity());
            deliveryItem.setAvgvalue(item.getPrice());
            deliveryItems.add(deliveryItem);
        }


        itemService.remove(Wrappers.<DeliveryItem>lambdaQuery().eq(DeliveryItem::getDelid, delivery.getDelid()));

        itemService.saveBatch(deliveryItems);
        return true;
    }

    @Override
    @Transactional
    public void deleteById(Delivery delivery) {
        Long delid = delivery.getDelid();
        if (delid == null) {
            return;
        }

        itemService.remove(Wrappers.<DeliveryItem>lambdaQuery().eq(DeliveryItem::getDelid, delid));

        removeById(delid);
    }
}
