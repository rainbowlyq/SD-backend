package com.packages.utils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

public class QueryUtils {
    public static RowMapper<Map<String, Object>> genericRowMapper() {
        return (rs, rowNum) -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, Object> result = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                Object columnValue = rs.getObject(i);
                result.put(columnName, columnValue);
            }
            return result;
        };
    }
}
