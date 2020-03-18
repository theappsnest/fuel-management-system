package com.godavari.appsnest.fms.report.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utility {

    public static String getMonthYear(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");
        return localDate.format(formatter) + " " + localDate.getYear();
    }

    public static final String dateFormatter(LocalDate localDate) {
        String pattern = "dd-MM-yyyy";
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }
}
