package com.godavari.appsnest.fms.dao.concrete.report;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.*;
import com.godavari.appsnest.fms.dao.model.*;
import com.godavari.appsnest.fms.dao.model.report.ReportFromToModel;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import com.godavari.appsnest.fms.dao.utility.UtilityMethod;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.VIEW_CURRENT_STOCK;

@Log4j
public class ReportFromToDaoImpl extends GenericDaoImpl<Object> {

    public static final List<String> projectionColumnNameList = DatabaseConstant.combineMultipleList(AccountContract.TABLE_COLUMN_NAME_LIST,
            HodManageContract.TABLE_COLUMN_NAME_LIST, VehicleAssignedContract.TABLE_COLUMN_NAME_LIST,
            DepartmentContract.TABLE_COLUMN_NAME_LIST, HeadOfDepartmentContract.TABLE_COLUMN_NAME_LIST,
            VehicleContract.TABLE_COLUMN_NAME_LIST, VehicleTypeContract.TABLE_COLUMN_NAME_LIST);

    public ReportFromToDaoImpl(Connection connection) {
        super(connection, null);
    }

    @Override
    public Object getRow(ResultSet resultSet) throws SQLException {
        return null;
    }

    public List<Account> getRowByFromToDate(ReportFromToModel reportFromToModel) throws SQLException {
        log.info("getRowByFromToDate, reportFromToModel: " + reportFromToModel);

        String[] projectionColumnArray = DatabaseConstant.convertListToArray(projectionColumnNameList);
        String[] viewName = {DatabaseConstant.VIEW_REPORT_FROM_TO};
        String query = DatabaseConstant.createGetRowByColumnNames(viewName, null, null);
        query += " Where " + AccountContract.COLUMN_DATE + " Between " + " ? " + " AND " + " ? ";

        System.out.println(query + " " + DatabaseConstant.localDateToEpoch(reportFromToModel.getFromLocalDate())
                + " " + DatabaseConstant.localDateToEpoch(reportFromToModel.getToLocalDate()));
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, DatabaseConstant.localDateToEpoch(reportFromToModel.getFromLocalDate()));
        preparedStatement.setLong(2, DatabaseConstant.localDateToEpoch(reportFromToModel.getToLocalDate()));

        ResultSet resultSet = preparedStatement.executeQuery();

        List<Account> accountList = new ArrayList<>();
        while (resultSet.next()) {
            accountList.add((Account) getRow(resultSet, projectionColumnNameList));
        }

        return accountList;
    }

    @Override
    public Object getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException {
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

        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(resultSet.getInt(projectionNameList.indexOf(VehicleTypeContract.TABLE_COLUMN_ID) + 1));
        vehicleType.setType(resultSet.getString(projectionNameList.indexOf(VehicleTypeContract.TABLE_COLUMN_TYPE) + 1));

        Vehicle vehicle = new Vehicle();
        vehicle.setId(resultSet.getInt(projectionNameList.indexOf(VehicleContract.TABLE_COLUMN_ID) + 1));
        vehicle.setVehicleNo(resultSet.getString(projectionNameList.indexOf(VehicleContract.TABLE_COLUMN_VEHICLE_NO) + 1));
        vehicle.setVehicleType(vehicleType);

        VehicleAssigned vehicleAssigned = new VehicleAssigned();
        vehicleAssigned.setId(resultSet.getInt(projectionNameList.indexOf(VehicleAssignedContract.TABLE_COLUMN_ID) + 1));
        vehicleAssigned.setVehicle(vehicle);
        vehicleAssigned.setDepartment(department);
        vehicleAssigned.setCurrent(resultSet.getBoolean(projectionNameList.indexOf(VehicleAssignedContract.TABLE_COLUMN_CURRENT) + 1));

        Account account = new Account();
        account.setId(resultSet.getInt(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_ID) + 1));
        account.setDateTime(DatabaseConstant.epochToLocalDateTime(resultSet.getLong(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_DATE) + 1)));
        account.setHodManage(hodManage);
        account.setVehicleAssigned(vehicleAssigned);
        account.setCurrentReading(resultSet.getDouble(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_CURRENT_READING) + 1));
        account.setInput(resultSet.getDouble(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_IN) + 1));
        account.setOutput(resultSet.getDouble(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_OUT) + 1));
        account.setOwner(resultSet.getString(projectionNameList.indexOf(AccountContract.TABLE_COLUMN_OWNER) + 1));

        return account;
    }

    public float getCurrentStockReading() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createGetAllRowQuery(VIEW_CURRENT_STOCK));
        ResultSet resultSet = preparedStatement.executeQuery();

        float currentStock = 0;
        if (resultSet.next()) {
            currentStock = resultSet.getFloat(1);
        }
        return currentStock;
    }
}
