package com.packages.controller;

import com.packages.entity.ContactPerson;
import com.packages.entity.Customer;
import com.packages.entity.MaterialSd;
import com.packages.service.ContactPersonService;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping("/person")
@RestController
public class ContactPersonCotroller {
    @Resource
    private ContactPersonService contactPersonService;
    @GetMapping("/getPersons")
    public List<ContactPerson> search(@RequestParam Map<String, String> params) {
        return contactPersonService.search(params);
    }
    @GetMapping("/getPersonsByname")
    public List<Map<String, Object>> searchByname(@RequestParam("name") String name) {
        return contactPersonService.searchByname(name);
    }
    @PostMapping("/insert")
    public int insert(@RequestBody ContactPerson ContactPerson) {
        return contactPersonService.insertcontactPerson(ContactPerson);
    }
    @PostMapping("/update")
    public int updateContactPerson(@RequestBody ContactPerson ContactPerson) {
        return contactPersonService.updateContactPerson(ContactPerson);
    }
}
