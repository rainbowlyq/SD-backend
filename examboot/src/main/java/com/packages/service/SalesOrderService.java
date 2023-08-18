package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.packages.entity.Delivery;
import com.packages.entity.Picking;
import com.packages.entity.Salesorder;
import com.packages.entity.Sell;
import com.packages.mapper.SalesorderMapper;
import com.packages.mapper.SellMapper;
import com.packages.utils.DateFormat;
import com.packages.utils.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalesOrderService extends BaseService<SalesorderMapper, Salesorder> {
    private final JdbcTemplate jdbcTemplate;
    private final SalesorderMapper salesorderMapper;
    private final SellMapper sellMapper;
    
    public SalesOrderService(JdbcTemplate jdbcTemplate, SalesorderMapper salesorderMapper, SellMapper sellMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.salesorderMapper = salesorderMapper;
        this.sellMapper = sellMapper;
    }
    
    public List<Salesorder> findAllSaleOrders(MultiValueMap<String, String> params) {
        QueryWrapper<Salesorder> queryWrapper = new QueryWrapper<>();
        
        // 根据参数构建查询条件
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String paramName = entry.getKey();
            List<String> paramValues = entry.getValue();
            if (paramValues.size() > 0 && paramValues.get(0) != "") {
                queryWrapper.in(paramName, paramValues.toArray());
            }
        }
        return salesorderMapper.selectList(queryWrapper);
    }
    
    public Salesorder findSalesOrderByQuoid(String inqid) {
        QueryWrapper<Salesorder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("salordid", inqid);
        return salesorderMapper.selectOne(queryWrapper);
    }
    
    public int updateSalesOrder(Salesorder salesorder) {
        UpdateWrapper<Salesorder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("salordid", salesorder.getSalordid());
        return salesorderMapper.update(salesorder, updateWrapper);
    }
    
    public int insertSalesOrder(Salesorder salesorder) {
        salesorder.setCreatedate(DateFormat.getTimeNow());
        salesorder.setStatus("ORD");
        salesorder.setDelissue("NPIC");
        salesorder.setInvissue("NINV");
        int rowsAffected = salesorderMapper.insert(salesorder);
        if (rowsAffected > 0) {
            return salesorder.getSalordid(); // 返回插入后生成的id
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }
    
    public int updateSell(Sell sell) {
        int count = 0;
        /*
        for (Sell sell : sells) {
            UpdateWrapper<Sell> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("salordid", sell.getSalordid());
            updateWrapper.eq("matid", sell.getMatid());
            count+=sellMapper.update(sell, updateWrapper);
        }
         */
        UpdateWrapper<Sell> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("salordid", sell.getSalordid());
        updateWrapper.eq("matid", sell.getMatid());
        count += sellMapper.update(sell, updateWrapper);
        return count;
    }
    
    public int insertSell(Sell sell) {
        int count = 0;
        /*
        for (Sell sell : sells) {
            count+=sellMapper.insert(sell);
        }
         */
        sellMapper.insert(sell);
        return count;
    }
    
    public List<Map<String, Object>> findSalesOrderItemBySalordid(String salordid) {
        String sql = "SELECT e.*, m.* FROM salesorder i " +
                "INNER JOIN sell e ON i.salordid = e.salordid " +
                "INNER JOIN material_sd m ON e.matid = m.msdId " +
                "WHERE i.salordid = ?";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> result = jdbcTemplate.query(sql, new Object[]{salordid}, rowMapper);
        return result;
    }
    
    public Map<String, List<Map<String, Object>>> getSearchCombination() {
        String sql1 = "SELECT DISTINCT sorg FROM salesorder";
        String sql2 = "SELECT DISTINCT dischannel FROM salesorder";
        String sql3 = "SELECT DISTINCT division FROM salesorder";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> sorg = jdbcTemplate.query(sql1, rowMapper);
        List<Map<String, Object>> dischannel = jdbcTemplate.query(sql2, rowMapper);
        List<Map<String, Object>> division = jdbcTemplate.query(sql3, rowMapper);
        Map<String, List<Map<String, Object>>> combinationMap = new HashMap<>();
        combinationMap.put("sorg", sorg);
        combinationMap.put("dischannel", dischannel);
        combinationMap.put("division", division);
        return combinationMap;
    }
    
    public List<Map<String, Object>> findfulfillment() {
        String sql = "SELECT netvalue as nov,salesorder.currency as cur,salordid,soldtoparty,customer.name as cus,\n" +
                "CASE\n" +
                "    WHEN delissue = 'NPIC' THEN 'Not Picked Yet'\n" +
                "    WHEN delissue = 'YPIC' THEN 'Partially Picked'\n" +
                "    WHEN delissue = 'APIC' THEN 'All Picked'\n" +
                "    WHEN delissue = 'NSTA' THEN 'Not yet Started'\n" +
                "    WHEN delissue = 'YSTA' THEN 'Partially Started'\n" +
                "    WHEN delissue = 'ASTA' THEN 'All Started'\n" +
                "\t\t\t\tELSE delissue\n" +
                "\t\t\t\tEND AS delissue,\n" +
                "CASE\n" +
                "\t\tWHEN invissue = 'NINV' THEN 'No Invoice Created'\n" +
                "    WHEN invissue = 'PINV' THEN 'Partially Invoice Created'\n" +
                "    WHEN invissue = 'AINV' THEN 'All Invoice Created'\n" +
                "    WHEN invissue = 'NREC' THEN 'No Receipt Issued'\n" +
                "    WHEN invissue = 'PREC' THEN 'Partially Receipt Issued'\n" +
                "    WHEN invissue = 'AREC' THEN 'All Receipt Issued'\n" +
                "\t\t\t\tELSE invissue\n" +
                "\t\t\t\tEND AS invissue,\n" +
                "CASE \n" +
                "    WHEN `status` = 'INV' THEN 'In Voice'\n" +
                "    WHEN `status` = 'ORD' THEN 'In Order'\n" +
                "    WHEN `status` = 'DLV' THEN 'In Delivery'\n" +
                "    WHEN `status` = 'FIN' THEN 'Finished'\n" +
                "        ELSE `status`\n" +
                "        END AS sta\n" +
                "from salesorder,customer\n" +
                "where salesorder.soldtoparty=customer.bp";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> result = jdbcTemplate.query(sql, rowMapper);
        return result;
    }
    
    public Salesorder getBySalordId(Integer salordid) {
        return salesorderMapper.selectById(salordid);
    }
    
    @Autowired
    DeliveryService deliveryService;
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    PickingService pickingService;
    
    public void updateSalesOrderStatus(Salesorder salesorder) {
        Integer salordid = salesorder.getSalordid();
        salesorder = getBySalordId(salordid);
        salesorder.setDeliveryList(deliveryService.findAllBySalOrdId(salordid));
        for (Delivery delivery : salesorder.getDeliveryList()) {
            Picking picking = new Picking();
            picking.setDelid(delivery.getDelid());
            delivery.setPickings(pickingService.search(picking));
        }
        Map<String, String> salordParams = new HashMap<>();
        salordParams.put("salordid", salordid.toString());
        salesorder.setInvoiceList(invoiceService.findAllInvoices(salordParams));
        Map<String, Object> salordParams1 = new HashMap<>();
        salordParams1.put("salordid", salordid.toString());
        salesorder.setSellList(sellMapper.selectByMap(salordParams1));
        if (salesorder.updateLists()) {
            updateSalesOrder(salesorder);
        }
    }
}
