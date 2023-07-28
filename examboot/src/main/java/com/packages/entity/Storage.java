package com.packages.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Storage {
    private String time;
    private List<Map<String, String>> tableData;
}
