package com.packages.service;

import com.packages.entity.Delivery;
import com.packages.mapper.DeliveryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService extends BaseService<DeliveryMapper, Delivery> {
    @Override
    public List<Delivery> search(Delivery delivery) {
        return getBaseMapper().search(delivery);
    }
}
