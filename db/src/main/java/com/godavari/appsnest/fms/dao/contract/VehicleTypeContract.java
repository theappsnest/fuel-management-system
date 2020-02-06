package com.godavari.appsnest.fms.dao.contract;

import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;

import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.TABLE_VEHICLE_TYPE_STRING;

public class VehicleTypeContract extends BaseContract {
    public static final String COLUMN_TYPE = "type";

    public static final String TABLE_COLUMN_ID = DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_TYPE_STRING, COLUMN_ID);
    public static final String TABLE_COLUMN_TYPE = DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_TYPE_STRING, COLUMN_TYPE);
    public static final String TABLE_COLUMN_CREATED_AT = DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_TYPE_STRING,COLUMN_CREATED_AT);
    public static final String TABLE_COLUMN_UPDATED_AT = DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_TYPE_STRING,COLUMN_UPDATED_AT);

    public static List<String> COLUMN_NAME_LIST = new ArrayList<String>() {
        {
            addAll(BASE_COLUMN_NAME_LIST);
            add(COLUMN_TYPE);
        }
    };

    public static List<String> TABLE_COLUMN_NAME_LIST = new ArrayList<String>() {
        {
            for (String columnName : COLUMN_NAME_LIST) {
                add(DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_TYPE_STRING, columnName));
            }
        }
    };
}
