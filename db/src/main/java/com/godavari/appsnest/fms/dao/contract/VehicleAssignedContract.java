package com.godavari.appsnest.fms.dao.contract;

import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;

import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.TABLE_VEHICLE_ASSIGNED_STRING;

public class VehicleAssignedContract extends BaseContract {
    public static final String COLUMN_DEPT_ID = "dept_id";
    public static final String COLUMN_VEHICLE_ID = "vehicle_id";
    public static final String COLUMN_CURRENT = "current";

    public static final String TABLE_COLUMN_ID = DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_ASSIGNED_STRING, COLUMN_ID);
    public static final String TABLE_COLUMN_DEPT_ID = DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_ASSIGNED_STRING, COLUMN_DEPT_ID);
    public static final String TABLE_COLUMN_VEHICLE_ID = DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_ASSIGNED_STRING, COLUMN_VEHICLE_ID);
    public static final String TABLE_COLUMN_CURRENT = DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_ASSIGNED_STRING, COLUMN_CURRENT);
    public static final String TABLE_COLUMN_CREATED_AT = DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_ASSIGNED_STRING,COLUMN_CREATED_AT);
    public static final String TABLE_COLUMN_UPDATED_AT = DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_ASSIGNED_STRING,COLUMN_UPDATED_AT);

    public static List<String> COLUMN_NAME_LIST = new ArrayList<String>() {
        {
            addAll(BASE_COLUMN_NAME_LIST);
            add(COLUMN_DEPT_ID);
            add(COLUMN_VEHICLE_ID);
            add(COLUMN_CURRENT);
        }
    };

    public static List<String> TABLE_COLUMN_NAME_LIST = new ArrayList<String>() {
        {
            for (String columnName : COLUMN_NAME_LIST) {
                add(DatabaseConstant.createTableColumnNameConstant(TABLE_VEHICLE_ASSIGNED_STRING, columnName));
            }
        }
    };
}
