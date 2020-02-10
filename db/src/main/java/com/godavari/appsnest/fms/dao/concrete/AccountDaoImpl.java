package com.godavari.appsnest.fms.dao.concrete;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.*;
import com.godavari.appsnest.fms.dao.interfaces.IAccountDao;
import com.godavari.appsnest.fms.dao.model.*;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.contract.AccountContract.*;
import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.*;

@Log4j
public class AccountDaoImpl extends GenericDaoImpl<Account> implements IAccountDao {

    public static final List<String> projectionNameList = DatabaseConstant.combineMultipleList(AccountContract.TABLE_COLUMN_NAME_LIST,
            HodManageContract.TABLE_COLUMN_NAME_LIST, VehicleAssignedContract.TABLE_COLUMN_NAME_LIST,
            DepartmentContract.TABLE_COLUMN_NAME_LIST, HeadOfDepartmentContract.TABLE_COLUMN_NAME_LIST,
            VehicleContract.TABLE_COLUMN_NAME_LIST, VehicleTypeContract.TABLE_COLUMN_NAME_LIST);

    public AccountDaoImpl(Connection connection) {
        super(connection, TABLE_ACCOUNT_STRING);
    }

    @Override
    public Account insert(Account account) throws SQLException {
        log.info("insert, account: " + account);
        String[] columnArray = new String[]{COLUMN_HOD_MANAGE_ID, COLUMN_VEHICLE_ASSIGNED_ID, COLUMN_DATE, COLUMN_CURRENT_READING, COLUMN_IN, COLUMN_OUT, COLUMN_OWNER
                , COLUMN_MILEAGE_KM_PER_HOUR, COLUMN_CREATED_AT};
        String insertQuery = DatabaseConstant.createInsertRowQuery(tableName, columnArray);

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setInt(1, account.getHodManage().getId());

        if (account.getVehicleAssigned() != null) {
            preparedStatement.setInt(2, account.getVehicleAssigned().getId());
        } else {
            preparedStatement.setNull(2, java.sql.Types.INTEGER);
        }

        preparedStatement.setLong(3, DatabaseConstant.localDateTimeToEpoch(account.getDateTime()));
        preparedStatement.setDouble(4, account.getCurrentReading());
        preparedStatement.setDouble(5, account.getInput());
        preparedStatement.setDouble(6, account.getOutput());
        preparedStatement.setString(7, account.getOwner());
        preparedStatement.setDouble(8, account.getMileageKmPerHour());
        preparedStatement.setLong(9, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        int idGenerated = resultSet.getInt(1);
        account.setId(idGenerated);
        resultSet.close();

        preparedStatement.close();

        return account;
    }

    @Override
    public List<Account> getAll() throws SQLException {
        String query = "SELECT * FROM "+DatabaseConstant.TABLE_ACCOUNT_STRING;
        query += " INNER JOIN " + TABLE_HOD_MANAGE_STRING + " ON " + TABLE_COLUMN_HOD_MANAGE_ID + " = " + HodManageContract.TABLE_COLUMN_ID;
        query += " LEFT OUTER JOIN " + TABLE_VEHICLE_ASSIGNED_STRING + " ON " + TABLE_COLUMN_VEHICLE_ASSIGNED_ID + " = " + VehicleAssignedContract.TABLE_COLUMN_ID;
        query += " LEFT OUTER JOIN " + TABLE_DEPARTMENT_STRING + " ON " + HodManageContract.TABLE_COLUMN_DEPT_ID + " = " + DepartmentContract.TABLE_COLUMN_ID;
        query += " INNER JOIN " + TABLE_HEAD_OF_DEPARTMENT_STRING + " ON " + HodManageContract.TABLE_COLUMN_HOD_ID + " = " + HeadOfDepartmentContract.TABLE_COLUMN_ID;
        query += " LEFT OUTER JOIN " + TABLE_VEHICLE_STRING + " ON " + VehicleAssignedContract.TABLE_COLUMN_VEHICLE_ID + " = " + VehicleContract.TABLE_COLUMN_ID;
        query += " LEFT OUTER JOIN " + TABLE_VEHICLE_TYPE_STRING + " ON " + VehicleContract.TABLE_COLUMN_VEHICLE_TYPE_ID + " = " + VehicleTypeContract.TABLE_COLUMN_ID;
        query += " ORDER BY " + TABLE_COLUMN_DATE + " ASC ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Account> accountList = new ArrayList<>();
        while (resultSet.next()) {
            accountList.add(getRow(resultSet,projectionNameList));
        }

        return accountList;
    }

    @Override
    public Account getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException {
        HeadOfDepartment headOfDepartment = new HeadOfDepartment();
        headOfDepartment.setId(resultSet.getInt(projectionNameList.indexOf(HeadOfDepartmentContract.TABLE_COLUMN_ID) + 1));
        headOfDepartment.setName(resultSet.getString(projectionNameList.indexOf(HeadOfDepartmentContract.TABLE_COLUMN_NAME) + 1));

        Department department = new Department();
        department.setId(resultSet.getInt(projectionNameList.indexOf(DepartmentContract.TABLE_COLUMN_ID) + 1));
        department.setName(resultSet.getString(projectionNameList.indexOf(DepartmentContract.TABLE_COLUMN_NAME) + 1));

        HodManage hodManage = new HodManage();
        hodManage.setId(resultSet.getInt(projectionNameList.indexOf(HodManageContract.TABLE_COLUMN_ID) + 1));
        hodManage.setHeadOfDepartment(headOfDepartment);
        hodManage.setDepartment(department);
        hodManage.setCurrent(resultSet.getBoolean(projectionNameList.indexOf(HodManageContract.TABLE_COLUMN_CURRENT) + 1));

        VehicleAssigned vehicleAssigned = null;
        int vehicleAssignedId = resultSet.getInt(projectionNameList.indexOf(VehicleAssignedContract.TABLE_COLUMN_ID) + 1);
        if (vehicleAssignedId>0)
        {
            VehicleType vehicleType = new VehicleType();
            vehicleType.setId(resultSet.getInt(projectionNameList.indexOf(VehicleTypeContract.TABLE_COLUMN_ID) + 1));
            vehicleType.setType(resultSet.getString(projectionNameList.indexOf(VehicleTypeContract.TABLE_COLUMN_TYPE) + 1));

            Vehicle vehicle = new Vehicle();
            vehicle.setId(resultSet.getInt(projectionNameList.indexOf(VehicleContract.TABLE_COLUMN_ID) + 1));
            vehicle.setVehicleNo(resultSet.getString(projectionNameList.indexOf(VehicleContract.TABLE_COLUMN_VEHICLE_NO) + 1));
            vehicle.setVehicleType(vehicleType);

            vehicleAssigned = new VehicleAssigned();
            vehicleAssigned.setId(resultSet.getInt(projectionNameList.indexOf(VehicleAssignedContract.TABLE_COLUMN_ID) + 1));
            vehicleAssigned.setVehicle(vehicle);
            vehicleAssigned.setDepartment(department);
            vehicleAssigned.setCurrent(resultSet.getBoolean(projectionNameList.indexOf(VehicleAssignedContract.TABLE_COLUMN_CURRENT) + 1));
        }

        Account account = new Account();
        account.setId(resultSet.getInt(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_ID) + 1));
        account.setDateTime(DatabaseConstant.epochToLocalDateTime(resultSet.getLong(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_DATE) + 1)));
        account.setHodManage(hodManage);
        account.setVehicleAssigned(vehicleAssigned);
        account.setCurrentReading(resultSet.getDouble(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_CURRENT_READING) + 1));
        account.setInput(resultSet.getDouble(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_IN) + 1));
        account.setOutput(resultSet.getDouble(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_OUT) + 1));
        account.setMileageKmPerHour(resultSet.getDouble(projectionNameList.indexOf(TABLE_COLUMN_MILEAGE_KM_PER_HOUR)+1));
        account.setOwner(resultSet.getString(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_OWNER) + 1));

        return account;
    }

    @Override
    public Account getRow(ResultSet resultSet) throws SQLException {
        throw  DatabaseConstant.getThrowsFunctionNotSupported(tableName, "resultSet");
    }

    @Override
    public int update(Account account) throws SQLException {
        String[] whereColumnNames = {COLUMN_ID};
        String[] valueUpdateColumnName = new String[]{COLUMN_HOD_MANAGE_ID, COLUMN_VEHICLE_ASSIGNED_ID, COLUMN_DATE, COLUMN_CURRENT_READING, COLUMN_IN, COLUMN_OUT, COLUMN_OWNER,
                COLUMN_MILEAGE_KM_PER_HOUR, COLUMN_UPDATED_AT};

        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createUpdateQuery(tableName, whereColumnNames, valueUpdateColumnName));
        preparedStatement.setInt(1, account.getHodManage().getId());
        if (account.getVehicleAssigned() != null) {
            preparedStatement.setInt(2, account.getVehicleAssigned().getId());
        } else {
            preparedStatement.setNull(2, java.sql.Types.INTEGER);
        }

        preparedStatement.setLong(3, DatabaseConstant.localDateTimeToEpoch(account.getDateTime()));
        preparedStatement.setDouble(4, account.getCurrentReading());
        preparedStatement.setDouble(5, account.getInput());
        preparedStatement.setDouble(6, account.getOutput());
        preparedStatement.setString(7, account.getOwner());
        preparedStatement.setLong(8, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));
        preparedStatement.setDouble(9, account.getMileageKmPerHour());
        //where condition value
        preparedStatement.setInt(10, account.getId());

        int rowAffected = preparedStatement.executeUpdate();

        return rowAffected;
    }

    @Override
    public Account getAccountByVehicleAndDateTime(Vehicle vehicle, LocalDateTime currentSelectedLocalDateTime) throws SQLException {
        log.info("getAccountByVehicle, vehicle: " + vehicle + ", currentSelectedLocalDateTime: " +
                DatabaseConstant.localDateTimeToEpoch(currentSelectedLocalDateTime));
        String[] viewName = {DatabaseConstant.VIEW_REPORT_FROM_TO};
        String query = DatabaseConstant.createGetRowByColumnNames(viewName, null, null);
        query += " Where " + VehicleContract.COLUMN_VEHICLE_NO + " =? " +
                " AND " + AccountContract.COLUMN_DATE + " < ? " +
                " ORDER BY " + AccountContract.COLUMN_DATE + " DESC";

        System.out.println(query);

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, vehicle.getVehicleNo());
        preparedStatement.setLong(2, DatabaseConstant.localDateTimeToEpoch(currentSelectedLocalDateTime));

        ResultSet resultSet = preparedStatement.executeQuery();
        Account account = null;
        if (resultSet.next()) {
            account = getRow(resultSet, projectionNameList);
        }

        return account;
    }
}
