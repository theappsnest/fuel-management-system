package com.godavari.appsnest.fms.dao.contract;

import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.TABLE_DEPARTMENT_STRING;

public class ReportMasterContract {
    public static final String COLUMN_MONTH_YEAR = "month_year";
    public static final String COLUMN_MONTHLY_OPENING = "monthly_opening";
    public static final String COLUMN_INPUT = "input";
    public static final String COLUMN_OUTPUT = "output";
    public static final String COLUMN_MONTHLY_CLOSING = "monthly_closing";


    public static List<String> COLUMN_NAME_LIST = new ArrayList<String>() {
        {
            add(COLUMN_MONTHLY_OPENING);
            add(COLUMN_INPUT);
            add(COLUMN_OUTPUT);
            add(COLUMN_MONTHLY_CLOSING);
        }
    };

}
