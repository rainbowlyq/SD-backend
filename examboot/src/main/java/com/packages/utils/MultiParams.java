package com.packages.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
@Component
public class MultiParams {
    public String buildDynamicSQL(String baseSQL, MultiValueMap<String, String> params) {
        StringBuilder sql = new StringBuilder(baseSQL);
        boolean isFirstCondition = true;

        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String paramName = entry.getKey();
            List<String> paramValues = entry.getValue();

            // 处理参数和对应的值
            if (paramValues.size() > 1) {
                if (!isFirstCondition) {
                    sql.append(" AND ");
                } else {
                    sql.append(" WHERE ");
                    isFirstCondition = false;
                }

                sql.append(paramName).append(" IN (");
                for (int i = 0; i < paramValues.size(); i++) {
                    String paramValue = paramValues.get(i);
                    sql.append("'").append(paramValue).append("'");
                    if (i < paramValues.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(")");
            } else if (paramValues.size() == 1) {
                if (!isFirstCondition) {
                    sql.append(" AND ");
                } else {
                    sql.append(" WHERE ");
                    isFirstCondition = false;
                }

                sql.append(paramName).append(" = '").append(paramValues.get(0)).append("'");
            }
        }

        return sql.toString();
    }

}
