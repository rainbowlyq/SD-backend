package com.packages.controller;

import com.packages.entity.Customer;
import com.packages.entity.MaterialSd;
import com.packages.entity.Relationship;
import com.packages.entity.Storage;
import com.packages.service.MaterialService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
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
    public int updateStorage(HttpServletRequest request, @RequestBody Storage storage) throws ParseException {
        HttpSession session = request.getSession();
//        int uid = (int) session.getAttribute("uid");
        int uid = 1;//登录做完得删掉
        String time = storage.getTime();
        List<Map<String, String>> tableData = storage.getTableData();
        System.out.println(tableData);
        return materialService.updateStorage(time, tableData, uid);
    }

    @GetMapping("/searchStorage")
    public List<Map<String, String>> searchStorage( @RequestParam("mid") String mid, @RequestParam("plant") String plant ) throws ParseException {
        return materialService.searchStorage(mid,plant);
    }
}
