package com.packages.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

public class Materialinventory {
    private String mid;
    private String plant;
    private String storageloc;
    private Integer unrestricted;
    private Integer salesorder;
    private Integer schedfordel;
}
