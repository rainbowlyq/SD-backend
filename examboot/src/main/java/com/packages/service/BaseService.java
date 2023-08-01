package com.packages.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements InitializingBean {
    protected Class<T> entityClass;

    protected TableInfo tableInfo;

    protected Field idField;

    protected BaseService() {
        this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        this.idField = this.entityClass.getDeclaredField(tableInfo.getKeyProperty());
        this.idField.setAccessible(true);
    }

    protected Serializable getId(T t) {
        if (t == null) {
            return null;
        }
        try {
            return (Serializable) idField.get(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> search(T t) {
        return list(Wrappers.query(t));
    }

    public T getById(T t) {
        return getById(getId(t));
    }

    public boolean createOrUpdate(@RequestBody T t) {
        Serializable id = getId(t);
        if (id == null) {
            save(t);
        } else {
            updateById(t);
        }
        return false;
    }


    public void deleteById(T t) {
        Serializable id = getId(t);
        if (id == null) {
            return;
        }
        removeById(id);
    }

}
