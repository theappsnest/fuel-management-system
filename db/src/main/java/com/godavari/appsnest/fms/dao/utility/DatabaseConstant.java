package com.godavari.appsnest.fms.dao.utility;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseConstant {

    public static final String THROWS_FUNCTION_NOT_SUPPORTED = "Function not supported, implement it";

    public static final String TABLE_ACCOUNT_STRING = "account";
    public static final String TABLE_DEPARTMENT_STRING = "department";
    public static final String TABLE_HEAD_OF_DEPARTMENT_STRING = "hod";
    public static final String TABLE_HOD_MANAGE_STRING = "hod_manage";
    public static final String TABLE_VEHICLE_STRING = "vehicle";
    public static final String TABLE_VEHICLE_ASSIGNED_STRING = "vehicle_assigned";
    public static final String TABLE_VEHICLE_TYPE_STRING = "vehicle_type";
    public static final String TABLE_ZZZ_STRING = "zzz";

    public static final String VIEW_REPORT_FROM_TO = "report_from_to_view";
    public static final String VIEW_REPORT_MASTER = "report_master_view";
    public static final String VIEW_CURRENT_STOCK = "current_stock_view";

    public static final String OPERATION_SELECT = "SELECT";
    public static final String OPERATION_INSERT = "INSERT";
    public static final String OPERATION_UPDATE = "UPDATE";
    public static final String OPERATION_DELETE = "DELETE";
    public static final String OPERATION_GET_ALL_COLUMN = "*";
    public static final String FROM_STRING = "FROM";
    public static final String INTO_STRING = "INTO";
    public static final String VALUES_STRING = "VALUES";
    public static final String EXISTS_STRING = "EXISTS";
    public static final String SET_STRING = "SET";
    public static final String CONDITION_WHERE_STRING = "WHERE";
    public static final String OPERATOR_AND = "AND";
    public static final String OPERATOR_OR = "OR";

    public static String createInsertRowQuery(String tableName, String[] columnNameArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OPERATION_INSERT + " ")
                .append(INTO_STRING + " ")
                .append(tableName);


        stringBuilder.append(" ( ");
        for (int i = 0; i < columnNameArray.length - 1; i++) {
            stringBuilder.append(columnNameArray[i] + ", ");
        }
        stringBuilder.append(columnNameArray[columnNameArray.length - 1]);
        stringBuilder.append(" ) ");

        stringBuilder.append(VALUES_STRING + " ");
        stringBuilder.append(" ( ");
        for (int i = 0; i < columnNameArray.length - 1; i++) {
            stringBuilder.append("?" + ", ");
        }
        stringBuilder.append("?")
                .append(" ) ");

        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    public static String createIsRowExistQuery(String tableName, String[] whereColumnNames) {
        //String query = "SELECT EXISTS(SELECT * from Receipt WHERE ChallanNo=" + "\"challaNo\"" + ");";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OPERATION_SELECT + " ")
                .append(EXISTS_STRING + " ")
                .append("(")
                .append(createGetRowByColumnNames(new String[]{tableName}, null, whereColumnNames));
        stringBuilder.append(");");

        return stringBuilder.toString();
    }

    public static String createGetRowByColumnNames(String[] tableNameArray, @Nullable String[] projectionNameArray, String[] whereColumnNames) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OPERATION_SELECT + " ");

        if (projectionNameArray == null || projectionNameArray.length == 0) {
            stringBuilder.append(OPERATION_GET_ALL_COLUMN + " ");
        } else {
            for (int i = 0; i < projectionNameArray.length - 1; i++) {
                stringBuilder.append(projectionNameArray[i] + ", ");
            }
            stringBuilder.append(projectionNameArray[projectionNameArray.length - 1] + " ");
        }

        stringBuilder.append(FROM_STRING + " ");

        for (int i = 0; i < tableNameArray.length - 1; i++) {
            stringBuilder.append(tableNameArray[i] + ", ");
        }
        stringBuilder.append(tableNameArray[tableNameArray.length - 1] + " ");

        if (whereColumnNames != null && whereColumnNames.length>0)
        {
            stringBuilder.append(CONDITION_WHERE_STRING + " ");
            for (int i = 0; i < whereColumnNames.length - 1; i++) {
                stringBuilder.append(whereColumnNames[i] + " = " + "?" + " AND ");
            }
            stringBuilder.append(whereColumnNames[whereColumnNames.length - 1] + " = " + "?");
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static String createGetRowByIdColumnName(String tableName, String[] projectionNameArray, String whereColumnName) {

        return createGetRowByColumnNames(new String[]{tableName}, projectionNameArray, new String[]{whereColumnName});
        /*StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OPERATION_SELECT + " ");

        if (projectionNameArray == null || projectionNameArray.length == 0) {
            stringBuilder.append(OPERATION_GET_ALL_COLUMN + " ");
        } else {
            for (int i = 0; i < projectionNameArray.length - 1; i++) {
                stringBuilder.append(projectionNameArray[i] + ", ");
            }
            stringBuilder.append(projectionNameArray[projectionNameArray.length - 1] + " ");
        }

        stringBuilder.append(FROM_STRING + " ")
                .append(tableName + " ")
                .append(CONDITION_WHERE_STRING + " ")
                .append(whereColumnName + " = " + "?");

        return stringBuilder.toString();*/
    }

    public static String createGetAllRowQuery(String tableName) {
        return OPERATION_SELECT + " " + OPERATION_GET_ALL_COLUMN + " " + FROM_STRING + " " + tableName + ";";
    }

    public static String createDeleteARowByIdQuery(String tableName, String columnName, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OPERATION_DELETE + " ")
                .append(FROM_STRING + " ")
                .append(tableName + " ")
                .append(CONDITION_WHERE_STRING + " ")
                .append(columnName + " = " + id)
                .append(";");

        return stringBuilder.toString();
    }

    public static String createUpdateQuery(String tableName, String[] whereColumnName, String[] valueUpdateColumnName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OPERATION_UPDATE + " ")
                .append(tableName + " ")
                .append(SET_STRING + " ");

        for (int i = 0; i < valueUpdateColumnName.length - 1; i++) {
            stringBuilder.append(valueUpdateColumnName[i] + " = " + " ? " + " , ");
        }
        stringBuilder.append(valueUpdateColumnName[valueUpdateColumnName.length - 1] + " = " + " ? ");

        stringBuilder.append(CONDITION_WHERE_STRING + " ");
        for (int i = 0; i < whereColumnName.length - 1; i++) {
            stringBuilder.append(whereColumnName[i] + " = " + " ? " + OPERATOR_AND + " ");
        }
        stringBuilder.append(whereColumnName[whereColumnName.length - 1] + " = " + " ? ")
                .append(";");

        return stringBuilder.toString();
    }

    public static String createTableColumnNameConstant(String tableName, String columnName) {
        return tableName + "." + columnName;
    }

    public static String[] combineMultipleArray(List<String>... tableColumnNames) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < tableColumnNames.length; i++) {
            list.addAll(tableColumnNames[i].subList(0, tableColumnNames[i].size()));
        }

        Object[] objectArray = list.toArray();
        return Arrays.copyOf(objectArray, objectArray.length, String[].class);
    }

    public static ArrayList<String> combineMultipleList(List<String> ...tableColumnNames)
    {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < tableColumnNames.length; i++) {
            list.addAll(tableColumnNames[i].subList(0, tableColumnNames[i].size()));
        }

        return list;
    }

    public static String[] convertListToArray(List<String> list)
    {
        Object[] objectArray = list.toArray();
        return Arrays.copyOf(objectArray, objectArray.length, String[].class);
    }

    public static SQLException getThrowsFunctionNotSupported(String tableName, String functionName)
    {
        return new SQLException("table name: "+tableName+", functionName: "+functionName+", "+THROWS_FUNCTION_NOT_SUPPORTED);
    }

    public static long localDateTimeToEpoch(LocalDateTime localDateTime)
    {
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public static LocalDateTime epochToLocalDateTime(long epochTime)
    {
        Instant instant = Instant.ofEpochSecond(epochTime);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate epochToLocalDate(long epochTime)
    {
        Instant instant = Instant.ofEpochSecond(epochTime);
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }


    public static long localDateToEpoch(LocalDate localDate)
    {
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return instant.getEpochSecond();
    }

    public static String strftimeFormat(String format)
    {
        //return " strftime('%m %Y', datetime("+" ? "+", 'unixepoch')) ";
        return strftimeFormat("%m %Y", "?");
    }

    public static String strftimeFormat(String format, String columnName)
    {
        return " strftime(\""+format+"\", datetime( "+columnName+" , 'unixepoch'))";
    }

    public static void main(String[] args) {
        System.out.println(differenceBetweenDates(LocalDate.now(), LocalDate.now().plusMonths(30)));
    }
    public static int differenceBetweenDates(LocalDate fromLocalDate, LocalDate toLocalDate)
    {
        System.out.println("fromLocalDate: "+fromLocalDate+", toLocalDate: "+toLocalDate);
        Period period = Period.between(fromLocalDate, toLocalDate);
        int diffYear = period.getYears();
        int diffMonth = diffYear * 12 + period.getMonths();
        return diffMonth;
    }
}
