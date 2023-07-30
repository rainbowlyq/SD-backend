package com.packages.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.packages.entity.Customer;
import com.packages.entity.MaterialSd;
import com.packages.entity.Relationship;
import com.packages.mapper.CustomerMapper;
import com.packages.mapper.MaterialMapper;
import com.packages.utils.QueryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MaterialService {
    private final JdbcTemplate jdbcTemplate;
    private final MaterialMapper materialMapper;

    public MaterialService(JdbcTemplate jdbcTemplate, MaterialMapper materialMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.materialMapper = materialMapper;
    }

    public Map<String, List<Map<String, Object>>> getSearchCombination() {
        String sql1 = "SELECT DISTINCT bomUnit bomUnit FROM material_sd";
        String sql2 = "SELECT DISTINCT weightUnit weightUnit FROM material_sd";
        String sql3 = "SELECT DISTINCT delStorPlant delStorPlant FROM material_sd";
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        List<Map<String, Object>> bom_unit = jdbcTemplate.query(sql1, rowMapper);
        List<Map<String, Object>> weight_unit = jdbcTemplate.query(sql2, rowMapper);
        List<Map<String, Object>> del_stor_plant = jdbcTemplate.query(sql3, rowMapper);
        Map<String, List<Map<String, Object>>> combinationMap = new HashMap<>();
        combinationMap.put("bomUnit", bom_unit);
        combinationMap.put("weightUnit", weight_unit);
        combinationMap.put("delStorPlant", del_stor_plant);
        return combinationMap;
    }

    public List<MaterialSd> findAllMaterials(Map<String, String> params) {
        QueryWrapper<MaterialSd> queryWrapper = new QueryWrapper<>();
        // 根据参数构建查询条件
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            if (paramValue != "") {
                queryWrapper.eq(paramName, paramValue);
            }
        }
        return materialMapper.selectList(queryWrapper);
    }

    public String getMid(String mid, String SalesOrg, String DistrChannel) {
        String sql1 = "SELECT mid FROM material_sd WHERE mid LIKE ? AND salesorg=? AND distrchannel=?";
        return jdbcTemplate.queryForObject(sql1, String.class, "%" + mid + "%", SalesOrg, DistrChannel);
    }

    public int insertMaterials(MaterialSd MaterialSd) {
        int rowsAffected = materialMapper.insert(MaterialSd);
        if (rowsAffected > 0) {
            return 1; // 成功
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }

    public int updateMaterials(MaterialSd MaterialSd) {
        int rowsAffected = materialMapper.updateById(MaterialSd);
        if (rowsAffected > 0) {
            return 1; // 成功
        } else {
            return -1; // 或者根据需求返回其他表示插入失败的值
        }
    }

    //    time: '',
//tableData: [
//{Material: '', Quantity: null, Plant: '', SLoc: ''},
//],
    public int updateStorage(String time, List<Map<String, String>> MI, int uid) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String timezone = "GMT+8";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        Date date = sdf.parse(time);
        for (Map<String, String> mi : MI) {
            String material = mi.get("Material");
            int quantity = Integer.parseInt(mi.get("Quantity"));
            String plant = mi.get("Plant");
            String sLoc = mi.get("SLoc");
            String sql = "INSERT INTO confirmreceipt(uid,mid,plant,StorageLoc,amount,time) values(?,?,?,?,?,?)";
            int rowsAffected1 = jdbcTemplate.update(sql, uid, material, plant, sLoc, quantity, date);
            //如果SQL语句执行失败（例如语法错误或约束冲突等），返回值通常为0。
            if (rowsAffected1 == 0) {
                return -1;
            }
            String sql1 = "SELECT Unrestricted FROM materialinventory WHERE Mid=? AND Plant=? AND StorageLoc=?";
            int oquantity = jdbcTemplate.queryForObject(sql1, Integer.class, material, plant, sLoc);
            int nquantity = oquantity + quantity;
            String sql2 = "UPDATE materialinventory\n" +
                    "SET Unrestricted = ?\n" +
                    "WHERE Mid=? AND Plant=? AND StorageLoc=?";
            int rowsAffected2 = jdbcTemplate.update(sql2, nquantity, material, plant, sLoc);
            if (rowsAffected2 == 0) {
                return -1;
            }
        }

        return 1;
    }

    public List<Map<String, String>> searchStorage(String mid,String plant){
        System.out.println(mid);
        System.out.println(plant);
        List<Map<String, String>> Mstorage = new ArrayList<>();
        Map<String, String> full = new HashMap<>();
        Map<String, String> cmp = new HashMap<>();
        Map<String, String> plt = new HashMap<>();
        Map<String, String> stl = new HashMap<>();
        Integer Unrestricted1 = 0;
        Integer SalesOrder1 = 0;
        Integer SchedForDel1 = 0;
        Integer Unrestricted2 = 0;
        Integer SalesOrder2 = 0;
        Integer SchedForDel2 = 0;
        Integer Unrestricted3 = 0;
        Integer SalesOrder3 = 0;
        Integer SchedForDel3 = 0;
        Integer Unrestricted4 = 0;
        Integer SalesOrder4 = 0;
        Integer SchedForDel4 = 0;
        String sql1 = "SELECT SUM(materialinventory.Unrestricted) as Unrestricted\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? ";
        String sql2 = "SELECT SUM(materialinventory.SalesOrder) as SalesOrder\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? ";
        String sql3 = "SELECT SUM(materialinventory.SchedForDel) as SchedForDel\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? ";
        Unrestricted1 = jdbcTemplate.queryForObject(sql1, Integer.class, mid);
        SalesOrder1 = jdbcTemplate.queryForObject(sql2, Integer.class, mid);
        SchedForDel1 = jdbcTemplate.queryForObject(sql3, Integer.class, mid);
        full.put("Unrestricted", String.valueOf(Unrestricted1));
        full.put("SalesOrder", String.valueOf(SalesOrder1));
        full.put("SchedForDel", String.valueOf(SchedForDel1));
        full.put("id","1");
        full.put("Client","full");
        Mstorage.add(full);

        String sql4 = "SELECT CONCAT(materialinventory.Plant,' ',plandescrp) as Client\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?\n" +
                "GROUP BY materialinventory.Plant,plandescrp";
        String sql5 = "SELECT SUM(materialinventory.Unrestricted) as Unrestricted\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?\n" +
                "GROUP BY materialinventory.Plant,plandescrp";
        String sql6 = "SELECT SUM(materialinventory.SalesOrder) as SalesOrder\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?\n" +
                "GROUP BY materialinventory.Plant,plandescrp";
        String sql7 = "SELECT SUM(materialinventory.SchedForDel) as SchedForDel\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?\n" +
                "GROUP BY materialinventory.Plant,plandescrp";
        Unrestricted2 = jdbcTemplate.queryForObject(sql5, Integer.class, mid,plant);
        SalesOrder2 = jdbcTemplate.queryForObject(sql6, Integer.class, mid,plant);
        SchedForDel2 = jdbcTemplate.queryForObject(sql7, Integer.class, mid,plant);
        String Client2 = jdbcTemplate.queryForObject(sql4, String.class, mid,plant);
        cmp.put("Unrestricted", String.valueOf(Unrestricted2));
        cmp.put("SalesOrder", String.valueOf(SalesOrder2));
        cmp.put("SchedForDel", String.valueOf(SchedForDel2));
        cmp.put("id","11");
        cmp.put("Client",Client2);
        Mstorage.add(cmp);


        String sql8 = "SELECT CONCAT(materialinventory.Plant,' ',plandescrp) as Client\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?\n" +
                "GROUP BY materialinventory.Plant,plandescrp";
        String sql9 = "SELECT SUM(materialinventory.Unrestricted) as Unrestricted\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?\n" +
                "GROUP BY materialinventory.Plant,plandescrp";
        String sql10 = "SELECT SUM(materialinventory.SalesOrder) as SalesOrder\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?\n" +
                "GROUP BY materialinventory.Plant,plandescrp";
        String sql11 = "SELECT SUM(materialinventory.SchedForDel) as SchedForDel\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?\n" +
                "GROUP BY materialinventory.Plant,plandescrp";
        Unrestricted3 = jdbcTemplate.queryForObject(sql9, Integer.class, mid,plant);
        SalesOrder3 = jdbcTemplate.queryForObject(sql10, Integer.class, mid,plant);
        SchedForDel3 = jdbcTemplate.queryForObject(sql11, Integer.class, mid,plant);
        String Client3 = jdbcTemplate.queryForObject(sql8, String.class, mid,plant);
        plt.put("Unrestricted", String.valueOf(Unrestricted3));
        plt.put("SalesOrder", String.valueOf(SalesOrder3));
        plt.put("SchedForDel", String.valueOf(SchedForDel3));
        plt.put("id","111");
        plt.put("Client",Client3);
        Mstorage.add(plt);


        String sql12 = "SELECT CONCAT(materialinventory.StorageLoc,' ',stodescrp) as Client\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?";
        String sql13 = "SELECT materialinventory.Unrestricted as Unrestricted\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?";
        String sql14 = "SELECT materialinventory.SalesOrder as SalesOrder\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?";
        String sql15 = "SELECT materialinventory.SchedForDel as SchedForDel\n" +
                "FROM materialinventory,plant_company\n" +
                "WHERE materialinventory.Plant = plant_company.delivering_plant \n" +
                "AND materialinventory.StorageLoc = plant_company.storage_loc\n" +
                "AND Mid=? AND Plant = ?";
        Unrestricted4 = jdbcTemplate.queryForObject(sql13, Integer.class, mid,plant);
        SalesOrder4 = jdbcTemplate.queryForObject(sql14, Integer.class, mid,plant);
        SchedForDel4 = jdbcTemplate.queryForObject(sql15, Integer.class, mid,plant);
        String Client4 = jdbcTemplate.queryForObject(sql12, String.class, mid,plant);
        stl.put("Unrestricted", String.valueOf(Unrestricted4));
        stl.put("SalesOrder", String.valueOf(SalesOrder4));
        stl.put("SchedForDel", String.valueOf(SchedForDel4));
        stl.put("id","1111");
        stl.put("Client",Client4);
        Mstorage.add(stl);


        return Mstorage;
    }
}
