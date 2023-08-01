package com.packages.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.packages.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class BaseController<T, S extends BaseService<M, T>, M extends BaseMapper<T>> {

    @Autowired
    protected S service;

    @PostMapping("/search")
    public List<T> search(@RequestBody T t) {
        return service.search(t);
    }

    @PostMapping("/getById")
    public T getById(@RequestBody T t) {
        return service.getById(t);
    }

    @PostMapping("/createOrUpdate")
    public boolean createOrUpdate(@RequestBody T t) {
        return service.createOrUpdate(t);
    }


    @PostMapping("/deleteById")
    public void deleteById(@RequestBody T t) {
        service.deleteById(t);
    }


}
