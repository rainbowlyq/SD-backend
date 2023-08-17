package com.packages.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.packages.controller.PickingController;
import com.packages.entity.*;
import com.packages.mapper.CustomerMapper;
import com.packages.mapper.DeliveryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;



    @Override
    public List<Delivery> search(Delivery delivery) {
        return getBaseMapper().search(delivery);
    }

    @Override
    public String createOrUpdate(Delivery delivery) {
        Integer salordid = delivery.getSalordid();
        Salesorder saleOrder = salesOrderService.getBySalordId(salordid);
        if (saleOrder == null) {
            return "该订单不存在，无法创建发货单";

        }

        Customer customer = customerMapper.selectOne(Wrappers.lambdaQuery(new Customer())
                .eq(Customer::getSrchterm, saleOrder.getCusref())
                .eq(Customer::getSaleOrg, saleOrder.getSorg())
                .eq(Customer::getDistrChannel, saleOrder.getDischannel())
                .eq(Customer::getDivision, saleOrder.getDivision())
        );

        Integer maxPartDeliveries = customer.getMaxPartDeliveries();

        if (maxPartDeliveries != null) {
            Delivery delivery1 = new Delivery();
            delivery1.setStatus(null);
            if (maxPartDeliveries <= count(Wrappers.lambdaQuery(delivery1).eq(Delivery::getSalordid, salordid))) {
                return "已达到该订单最大发货次数，无法新增发货单，请检查已创建的发货单";
            }
        }


        HashMap<String, Object> pickingPreview = new HashMap<>();
        pickingPreview.put("salordid", salordid);
        List<Map<String, Object>> preview = pickingController.preview0(pickingPreview);
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
    public void deleteById(Delivery delivery) {
        Long delid = delivery.getDelid();
        if (delid == null) {
            return;
        }
        jdbcTemplate.update("update materialinventory mi inner join (select ms.mid, ms.delstorplant, ms.storageloc, ifnull(count(p.quantity), 0) quantity " +
                "                                        from picking p " +
                "                                                 inner join material_sd ms on p.matid = ms.msdId and p.delid =  " + delid +
                "                                       group by ms.mid, ms.delstorplant, ms.storageloc) picked on mi.Mid = picked.mid and mi.Plant = picked.delstorplant and mi.StorageLoc = picked.storageloc " +
                "set mi.SchedForDel = mi.SchedForDel - picked.quantity");

        jdbcTemplate.update("update materialinventory mi inner join (select ms.mid, ms.delstorplant, ms.storageloc, ifnull(count(p.quantity), 0) quantity " +
                "                                        from picking p " +
                "                                                 inner join material_sd ms on p.matid = ms.msdId and p.delid =  " + delid +
                "                                       group by ms.mid, ms.delstorplant, ms.storageloc) picked on mi.Mid = picked.mid and mi.Plant = picked.delstorplant and mi.StorageLoc = picked.storageloc " +
                "set mi.SalesOrder = mi.SalesOrder + picked.quantity");
        removeById(delid);
        DeliveryItem deleteDeliveryItem = new DeliveryItem();
        deleteDeliveryItem.setDelid(delid);
        deliveryItemService.remove(Wrappers.query(deleteDeliveryItem));
        GoodsIssue deleteGi = new GoodsIssue();
        deleteGi.setDelid(delid);
        giService.remove(Wrappers.query(deleteGi));

        Salesorder saleorder = salesOrderService.getById(getById(delivery.getDelid()).getSalordid());
        salesOrderService.updateSalesOrderStatus(saleorder);
    }
    
    public List<Delivery> findAllBySalOrdId(int salordid){
        return getBaseMapper().findAllBySalOrdId(salordid);
    }
}
