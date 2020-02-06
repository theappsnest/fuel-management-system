package com.godavari.appsnest.fms.dao.contract;

import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;

import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.TABLE_ACCOUNT_STRING;

public class AccountContract extends BaseContract {
    public static final String COLUMN_HOD_MANAGE_ID = "hod_manage_id";
    public static final String COLUMN_VEHICLE_ASSIGNED_ID = "vehicle_assigned_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CURRENT_READING = "current_reading";
    public static final String COLUMN_IN = "input";
    public static final String COLUMN_OUT = "output";
    public static final String COLUMN_OWNER = "owner";
    public static final String COLUMN_MILEAGE_KM_PER_HOUR = "mileage_km_per_hour";

    public static final String TABLE_COLUMN_ID = DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING, COLUMN_ID);
    public static final String TABLE_COLUMN_HOD_MANAGE_ID = DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING, COLUMN_HOD_MANAGE_ID);
    public static final String TABLE_COLUMN_VEHICLE_ASSIGNED_ID = DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING, COLUMN_VEHICLE_ASSIGNED_ID);
    public static final String TABLE_COLUMN_DATE = DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING, COLUMN_DATE);
    public static final String TABLE_COLUMN_CURRENT_READING = DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING, COLUMN_CURRENT_READING);
    public static final String TABLE_COLUMN_IN = DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING, COLUMN_IN);
    public static final String TABLE_COLUMN_OUT = DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING, COLUMN_OUT);
    public static final String TABLE_COLUMN_OWNER = DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING, COLUMN_OWNER);
    public static final String TABLE_COLUMN_MILEAGE_KM_PER_HOUR =
            DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING, COLUMN_MILEAGE_KM_PER_HOUR);
    public static final String TABLE_COLUMN_CREATED_AT = DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING,COLUMN_CREATED_AT);
    public static final String TABLE_COLUMN_UPDATED_AT = DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING,COLUMN_UPDATED_AT);

    public static List<String> COLUMN_NAME_LIST = new ArrayList<String>() {
        {
            addAll(BASE_COLUMN_NAME_LIST);
            add(COLUMN_HOD_MANAGE_ID);
            add(COLUMN_VEHICLE_ASSIGNED_ID);
            add(COLUMN_DATE);
            add(COLUMN_CURRENT_READING);
            add(COLUMN_IN);
            add(COLUMN_OUT);
            add(COLUMN_OWNER);
            add(COLUMN_MILEAGE_KM_PER_HOUR);
            add(COLUMN_CREATED_AT);
        }
    };

    public static List<String> TABLE_COLUMN_NAME_LIST = new ArrayList<String>() {
        {
            for (String columnName : COLUMN_NAME_LIST) {
                add(DatabaseConstant.createTableColumnNameConstant(TABLE_ACCOUNT_STRING, columnName));
            }
        }
    };

}
