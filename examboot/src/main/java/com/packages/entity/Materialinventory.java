package com.packages.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

public class Materialinventory {
    @TableId
    private String mid;
    @TableId
    private String plant;
    @TableId
    private String storageloc;
    private Integer unrestricted;
    private Integer salesorder;
    private Integer schedfordel;
}
