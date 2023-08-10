package com.packages.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private int uid;   //uid
    private String uname;   //用户名
    private String pwd;   //密码
    private int client;    //client
}
