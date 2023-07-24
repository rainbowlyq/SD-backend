package com.packages.service;

import com.packages.mapper.CustomerMapper;
import com.packages.utils.QueryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChartService {
    private final JdbcTemplate jdbcTemplate;
    public ChartService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Map<String, Object>> getCountLine() {
        String sql = "WITH months AS (\n" +
                "  SELECT DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -(n - 1) MONTH), '%Y-%m') AS month\n" +
                "  FROM (\n" +
                "    SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL\n" +
                "    SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL\n" +
                "    SELECT 11 UNION ALL SELECT 12\n" +
                "  ) t\n" +
                "  WHERE DATE_ADD(NOW(), INTERVAL -(n - 1) MONTH) < LAST_DAY(NOW())\n" +
                "),\n" +
                "monthly_counts AS (\n" +
                "  SELECT \n" +
                "    DATE_FORMAT(createdate, '%Y-%m') AS month,\n" +
                "    COUNT(*) AS inquiry_count\n" +
                "  FROM inquiry\n" +
                "  WHERE createdate >= DATE_ADD(NOW(), INTERVAL -12 MONTH) AND createdate < DATE_ADD(NOW(), INTERVAL 1 MONTH)\n" +
                "  GROUP BY DATE_FORMAT(createdate, '%Y-%m')\n" +
                "),\n" +
                "quotation_counts AS (\n" +
                "  SELECT \n" +
                "    DATE_FORMAT(createdate, '%Y-%m') AS month,\n" +
                "    COUNT(*) AS quotation_count\n" +
                "  FROM quotation\n" +
                "  WHERE createdate >= DATE_ADD(NOW(), INTERVAL -12 MONTH) AND createdate < DATE_ADD(NOW(), INTERVAL 1 MONTH)\n" +
                "  GROUP BY DATE_FORMAT(createdate, '%Y-%m')\n" +
                "),\n" +
                "sales_order_counts AS (\n" +
                "  SELECT \n" +
                "    DATE_FORMAT(createdate, '%Y-%m') AS month,\n" +
                "    COUNT(*) AS sales_order_count\n" +
                "  FROM salesorder\n" +
                "  WHERE createdate >= DATE_ADD(NOW(), INTERVAL -12 MONTH) AND createdate < DATE_ADD(NOW(), INTERVAL 1 MONTH)\n" +
                "  GROUP BY DATE_FORMAT(createdate, '%Y-%m')\n" +
                ")\n" +
                "SELECT \n" +
                "  m.month,\n" +
                "  SUM(COALESCE(i.inquiry_count, 0)) OVER (ORDER BY m.month) AS inquiry_count,\n" +
                "  SUM(COALESCE(q.quotation_count, 0)) OVER (ORDER BY m.month) AS quotation_count,\n" +
                "  SUM(COALESCE(s.sales_order_count, 0)) OVER (ORDER BY m.month) AS salesorder_count\n" +
                "FROM months m\n" +
                "LEFT JOIN monthly_counts i ON m.month = i.month\n" +
                "LEFT JOIN quotation_counts q ON m.month = q.month\n" +
                "LEFT JOIN sales_order_counts s ON m.month = s.month;\n" ;
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        return jdbcTemplate.query(sql, rowMapper);
    }
    public List<Map<String, Object>> getOrderCount() {
        String sql = "WITH months AS (\n" +
                "  SELECT DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -(n - 1) MONTH), '%Y-%m') AS month\n" +
                "  FROM (\n" +
                "    SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL\n" +
                "    SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL\n" +
                "    SELECT 11 UNION ALL SELECT 12\n" +
                "  ) t\n" +
                "  WHERE DATE_ADD(NOW(), INTERVAL -(n - 1) MONTH) < LAST_DAY(NOW())\n" +
                ")\n" +
                "SELECT \n" +
                "    months.month,\n" +
                "    COALESCE(inquiry_count, 0) AS inquiry_count,\n" +
                "    COALESCE(quotation_count, 0) AS quotation_count,\n" +
                "    COALESCE(sales_order_count, 0) AS salesorder_count\n" +
                "FROM \n" +
                "    months\n" +
                "LEFT JOIN\n" +
                "    (\n" +
                "        SELECT DATE_FORMAT(createdate, '%Y-%m') AS month, COUNT(*) AS inquiry_count\n" +
                "        FROM inquiry\n" +
                "        WHERE createdate >= DATE_ADD(NOW(), INTERVAL -12 MONTH) AND createdate < DATE_ADD(NOW(), INTERVAL 1 MONTH)\n" +
                "        GROUP BY DATE_FORMAT(createdate, '%Y-%m')\n" +
                "    ) AS inquiry_table\n" +
                "ON months.month = inquiry_table.month\n" +
                "LEFT JOIN\n" +
                "    (\n" +
                "        SELECT DATE_FORMAT(createdate, '%Y-%m') AS month, COUNT(*) AS quotation_count\n" +
                "        FROM quotation\n" +
                "        WHERE createdate >= DATE_ADD(NOW(), INTERVAL -12 MONTH) AND createdate < DATE_ADD(NOW(), INTERVAL 1 MONTH)\n" +
                "        GROUP BY DATE_FORMAT(createdate, '%Y-%m')\n" +
                "    ) AS quotation_table\n" +
                "ON months.month = quotation_table.month\n" +
                "LEFT JOIN\n" +
                "    (\n" +
                "        SELECT DATE_FORMAT(createdate, '%Y-%m') AS month, COUNT(*) AS sales_order_count\n" +
                "        FROM salesorder\n" +
                "        WHERE createdate >= DATE_ADD(NOW(), INTERVAL -12 MONTH) AND createdate < DATE_ADD(NOW(), INTERVAL 1 MONTH)\n" +
                "        GROUP BY DATE_FORMAT(createdate, '%Y-%m')\n" +
                "    ) AS salesorder_table\n" +
                "ON months.month = salesorder_table.month\n" +
                "ORDER BY months.month;\n" ;
        RowMapper<Map<String, Object>> rowMapper = QueryUtils.genericRowMapper();
        return jdbcTemplate.query(sql, rowMapper);
    }
}
