package com.packages.utils;

import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DateFormat {
    public static String getTimeNow() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
}
