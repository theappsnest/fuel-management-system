package com.godavari.appsnest.fms.dao.contract;

import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;

import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.TABLE_HEAD_OF_DEPARTMENT_STRING;

public class HeadOfDepartmentContract extends BaseContract {
    public static final String COLUMN_NAME = "name";

    public static final String TABLE_COLUMN_ID = DatabaseConstant.createTableColumnNameConstant(TABLE_HEAD_OF_DEPARTMENT_STRING, COLUMN_ID);
    public static final String TABLE_COLUMN_NAME = DatabaseConstant.createTableColumnNameConstant(TABLE_HEAD_OF_DEPARTMENT_STRING, COLUMN_NAME);
    public static final String TABLE_COLUMN_CREATED_AT = DatabaseConstant.createTableColumnNameConstant(TABLE_HEAD_OF_DEPARTMENT_STRING,COLUMN_CREATED_AT);
    public static final String TABLE_COLUMN_UPDATED_AT = DatabaseConstant.createTableColumnNameConstant(TABLE_HEAD_OF_DEPARTMENT_STRING,COLUMN_UPDATED_AT);

    public static List<String> COLUMN_NAME_LIST = new ArrayList<String>() {
        {
            add(COLUMN_ID);
            add(COLUMN_NAME);
            add(COLUMN_CREATED_AT);
            add(COLUMN_UPDATED_AT);
        }
    };

    public static List<String> TABLE_COLUMN_NAME_LIST = new ArrayList<String>() {
        {
            for (String columnName : COLUMN_NAME_LIST) {
                add(DatabaseConstant.createTableColumnNameConstant(TABLE_HEAD_OF_DEPARTMENT_STRING, columnName));
            }
        }
    };
}
