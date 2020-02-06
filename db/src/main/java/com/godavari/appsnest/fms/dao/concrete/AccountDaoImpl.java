package com.godavari.appsnest.fms.dao.concrete;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.*;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.daofactory.DbSqlite;
import com.godavari.appsnest.fms.dao.interfaces.IAccountDao;
import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.HodManage;
import com.godavari.appsnest.fms.dao.model.VehicleAssigned;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.extern.log4j.Log4j;
import sun.rmi.runtime.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.contract.AccountContract.*;
import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.*;

@Log4j
public class AccountDaoImpl extends GenericDaoImpl<Account> implements IAccountDao {

    private static final String LOG_TAG = AccountDaoImpl.class.getSimpleName();

    public AccountDaoImpl(Connection connection) {
        super(connection, TABLE_ACCOUNT_STRING);
    }

    @Override
    public Account insert(Account account) throws SQLException {
        log.info("insert, account: " + account);
        String[] columnArray = new String[]{COLUMN_HOD_MANAGE_ID, COLUMN_VEHICLE_ASSIGNED_ID, COLUMN_DATE, COLUMN_CURRENT_READING, COLUMN_IN, COLUMN_OUT, COLUMN_OWNER
                , COLUMN_CREATED_AT};
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
        preparedStatement.setLong(8, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        int idGenerated = resultSet.getInt(1);
        account.setId(idGenerated);
        resultSet.close();

        preparedStatement.close();

        return account;
    }

    public List<Account> getAll1() throws SQLException {
        log.info("getAll1");
        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createGetAllRowQuery(tableName));
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Account> accountList = new ArrayList<>();

        while (resultSet.next()) {
            accountList.add(getRow(resultSet));
        }

        return accountList;
    }

    @Override
    public Account getRow(ResultSet resultSet) throws SQLException {
        DaoFactory daoFactory = DbSqlite.getDatabase();
        Account account = new Account();
        account.setId(resultSet.getInt(resultSet.findColumn(COLUMN_ID)));
        account.setDateTime(DatabaseConstant.epochToLocalDateTime(resultSet.getLong(resultSet.findColumn(COLUMN_DATE))));
        account.setHodManage(daoFactory.getHodManageDao().getRowById(resultSet.getInt(resultSet.findColumn(COLUMN_HOD_MANAGE_ID))));
        account.setVehicleAssigned(daoFactory.getVehicleAssignedDao().getRowById(resultSet.getInt(resultSet.findColumn(COLUMN_VEHICLE_ASSIGNED_ID))));
        account.setCurrentReading(resultSet.getDouble(resultSet.findColumn(COLUMN_CURRENT_READING)));
        account.setInput(resultSet.getDouble(resultSet.findColumn(COLUMN_IN)));
        account.setOutput(resultSet.getDouble(resultSet.findColumn(COLUMN_OUT)));
        account.setOwner(resultSet.getString(resultSet.findColumn(COLUMN_OWNER)));

        return account;
    }

    @Override
    public List<Account> getAll() throws SQLException {
        log.info("getAll");
        List<String> projectionNameList = DatabaseConstant.combineMultipleList(TABLE_COLUMN_NAME_LIST,
                HodManageContract.TABLE_COLUMN_NAME_LIST, VehicleAssignedContract.TABLE_COLUMN_NAME_LIST);

        String[] projectionNameArray = DatabaseConstant.convertListToArray(projectionNameList);
        String[] tableNameArray = {TABLE_ACCOUNT_STRING, TABLE_HOD_MANAGE_STRING, DatabaseConstant.TABLE_VEHICLE_ASSIGNED_STRING};
        String[] whereColumnArray = null;

        String query = DatabaseConstant.createGetRowByColumnNames(tableNameArray, projectionNameArray, whereColumnArray);
        if (whereColumnArray == null) {
            query += " " + CONDITION_WHERE_STRING + " ";
        } else {
            query += " AND ";
        }
        query += TABLE_COLUMN_HOD_MANAGE_ID + " = " + HodManageContract.TABLE_COLUMN_ID;
        query += " AND " + TABLE_COLUMN_VEHICLE_ASSIGNED_ID + " = " + VehicleAssignedContract.TABLE_COLUMN_ID + ";";

        System.out.println(query);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Account> accountArrayList = new ArrayList<>();
        while (resultSet.next()) {
            accountArrayList.add(getRow(resultSet, projectionNameList));
        }

        return accountArrayList;
    }

    @Override
    public Account getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException {

        VehicleAssigned vehicleAssigned = DaoFactory.getDatabase().getVehicleAssignedDao().getRow(resultSet, projectionNameList);
        HodManage hodManage = DaoFactory.getDatabase().getHodManageDao().getRow(resultSet, projectionNameList);

        Account account = new Account();
        account.setDateTime(DatabaseConstant.epochToLocalDateTime(resultSet.getLong(projectionNameList.indexOf(TABLE_COLUMN_DATE) + 1)));
        account.setVehicleAssigned(vehicleAssigned);
        account.setHodManage(hodManage);
        account.setCurrentReading(resultSet.getDouble(projectionNameList.indexOf(TABLE_COLUMN_CURRENT_READING) + 1));
        account.setInput(resultSet.getDouble(projectionNameList.indexOf(TABLE_COLUMN_IN) + 1));
        account.setInput(resultSet.getDouble(projectionNameList.indexOf(TABLE_COLUMN_OUT) + 1));
        account.setOwner(resultSet.getString(projectionNameList.indexOf(TABLE_COLUMN_OWNER) + 1));

        return account;
    }

    @Override
    public int update(Account account) throws SQLException {
        String[] whereColumnNames = {COLUMN_ID};
        String[] valueUpdateColumnName = new String[]{COLUMN_HOD_MANAGE_ID, COLUMN_VEHICLE_ASSIGNED_ID, COLUMN_DATE, COLUMN_CURRENT_READING, COLUMN_IN, COLUMN_OUT, COLUMN_OWNER,
        COLUMN_UPDATED_AT};

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
        preparedStatement.setLong(8,DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));
        //where condition value
        preparedStatement.setInt(9, account.getId());

        int rowAffected = preparedStatement.executeUpdate();

        return rowAffected;
    }
}
