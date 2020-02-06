package com.godavari.appsnest.fms.dao.abstracts;

import com.godavari.appsnest.fms.dao.contract.DepartmentContract;
import com.godavari.appsnest.fms.dao.contract.HeadOfDepartmentContract;
import com.godavari.appsnest.fms.dao.contract.VehicleContract;
import com.godavari.appsnest.fms.dao.contract.VehicleTypeContract;
import com.godavari.appsnest.fms.dao.interfaces.IGenericDao;
import com.godavari.appsnest.fms.dao.model.*;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.contract.BaseContract.COLUMN_ID;

/**
 * @param <T>
 */

@Log4j
public abstract class GenericDaoImpl<T> implements IGenericDao<T> {

    private static final String LOG_TAG = GenericDaoImpl.class.getSimpleName();

    protected Connection connection;
    protected String tableName;

    public GenericDaoImpl(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    @Override
    public T insert(T object) throws SQLException {
        throw DatabaseConstant.getThrowsFunctionNotSupported(tableName, "insert");
    }

    @Override
    public int update(T object) throws SQLException {
        throw DatabaseConstant.getThrowsFunctionNotSupported(tableName, "update");
    }

    @Override
    public List<T> getAll() throws SQLException {
        List<T> rowList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createGetAllRowQuery(tableName));

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            T row = getRow(resultSet);
            rowList.add(row);
        }

        preparedStatement.close();
        return rowList;
    }

    @Override
    public T getRowById(int id) throws SQLException
    {
        throw  DatabaseConstant.getThrowsFunctionNotSupported(tableName, "getRowById");
    }

    @Override
    public int deleteAll() throws SQLException {
        throw DatabaseConstant.getThrowsFunctionNotSupported(tableName, "deleteAll");
    }

    @Override
    public int delete(T object) throws SQLException {
        String query = "";
        switch (tableName) {
            case DatabaseConstant.TABLE_HEAD_OF_DEPARTMENT_STRING:
                HeadOfDepartment headOfDepartment = (HeadOfDepartment) object;
                query = DatabaseConstant.createDeleteARowByIdQuery(tableName, COLUMN_ID, headOfDepartment.getId());
                break;
            case DatabaseConstant.TABLE_DEPARTMENT_STRING:
                Department department = (Department) object;
                query = DatabaseConstant.createDeleteARowByIdQuery(tableName, COLUMN_ID, department.getId());
                break;
            case DatabaseConstant.TABLE_HOD_MANAGE_STRING:
                HodManage hodManage = (HodManage) object;
                query = DatabaseConstant.createDeleteARowByIdQuery(tableName, COLUMN_ID, hodManage.getId());
                break;
            case DatabaseConstant.TABLE_VEHICLE_STRING:
                Vehicle vehicle = (Vehicle) object;
                query = DatabaseConstant.createDeleteARowByIdQuery(tableName, COLUMN_ID, vehicle.getId());
                break;
            case DatabaseConstant.TABLE_VEHICLE_TYPE_STRING:
                VehicleType vehicleType = (VehicleType) object;
                query = DatabaseConstant.createDeleteARowByIdQuery(tableName, COLUMN_ID, vehicleType.getId());
                break;
            case DatabaseConstant.TABLE_VEHICLE_ASSIGNED_STRING:
                VehicleAssigned vehicleAssigned = (VehicleAssigned) object;
                query = DatabaseConstant.createDeleteARowByIdQuery(tableName, COLUMN_ID, vehicleAssigned.getId());
                break;
            case DatabaseConstant.TABLE_ACCOUNT_STRING:
                Account account = (Account) object;
                query = DatabaseConstant.createDeleteARowByIdQuery(tableName, COLUMN_ID, account.getId());
                break;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        int rowAffected = preparedStatement.executeUpdate();

        return rowAffected;
    }

    @Override
    public boolean isRowExistByStringColumn(String searchValue) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = null;
        String[] whereColumnNames;
        if (DatabaseConstant.TABLE_HEAD_OF_DEPARTMENT_STRING.equals(tableName)) {
            whereColumnNames = new String[]{HeadOfDepartmentContract.COLUMN_NAME};
            query = DatabaseConstant.createIsRowExistQuery(tableName, whereColumnNames);
        } else if (DatabaseConstant.TABLE_DEPARTMENT_STRING.equals(tableName)) {
            whereColumnNames = new String[]{DepartmentContract.COLUMN_NAME};
            query = DatabaseConstant.createIsRowExistQuery(tableName, whereColumnNames);
        } else if (DatabaseConstant.TABLE_VEHICLE_TYPE_STRING.equals(tableName)) {
            whereColumnNames = new String[]{VehicleTypeContract.COLUMN_TYPE};
            query = DatabaseConstant.createIsRowExistQuery(tableName, whereColumnNames);
        } else if (DatabaseConstant.TABLE_VEHICLE_STRING.equals(tableName)) {
            whereColumnNames = new String[]{VehicleContract.COLUMN_VEHICLE_NO};
            query = DatabaseConstant.createIsRowExistQuery(tableName, whereColumnNames);
        }

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, searchValue);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int value = resultSet.getInt(1);
        if (value == 1)
            return true;
        else
            return false;
    }

    @Override
    public T getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException {
        throw DatabaseConstant.getThrowsFunctionNotSupported(tableName, "getRow");
    }
}
