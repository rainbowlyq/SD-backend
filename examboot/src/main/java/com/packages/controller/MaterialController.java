package com.packages.controller;

import com.packages.entity.Customer;
import com.packages.entity.MaterialSd;
import com.packages.entity.Relationship;
import com.packages.service.MaterialService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@RequestMapping("/material")
@RestController
public class MaterialController {
    @Resource
    private MaterialService materialService;
    @GetMapping("/getSearchCombination")
    public Map<String, List<Map<String, Object>>> getSearchCombination() {
        return materialService.getSearchCombination();
    }

    @GetMapping("/getMaterials")
    public List<MaterialSd> getMaterials(@RequestParam Map<String, String> params) {
        return materialService.findAllMaterials(params);
    }

    @PostMapping("/insertMaterials")
    public int insertMaterials(@RequestBody MaterialSd MaterialSd) {
        return materialService.insertMaterials(MaterialSd);
    }

    @PostMapping("/updateMaterials")
    public int updateMaterials(@RequestBody MaterialSd MaterialSd) {
        return materialService.updateMaterials(MaterialSd);
    }

    //    time: '',
//tableData: [
//{Material: '', Quantity: null, Plant: '', SLoc: ''},
//],
    @PostMapping("/updateStorage")
    public int updateStorage(@RequestParam("time") String time, @RequestParam("tableData") List<Map<String, Object>> MI) {
        return materialService.updateStorage(time,MI);
    }
}
