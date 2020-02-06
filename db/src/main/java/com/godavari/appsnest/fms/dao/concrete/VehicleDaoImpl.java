package com.godavari.appsnest.fms.dao.concrete;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.VehicleContract;
import com.godavari.appsnest.fms.dao.contract.VehicleTypeContract;
import com.godavari.appsnest.fms.dao.interfaces.IVehicleDao;
import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.model.Vehicle;
import com.godavari.appsnest.fms.dao.model.VehicleAssigned;
import com.godavari.appsnest.fms.dao.model.VehicleType;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.contract.BaseContract.COLUMN_CREATED_AT;
import static com.godavari.appsnest.fms.dao.contract.BaseContract.COLUMN_UPDATED_AT;
import static com.godavari.appsnest.fms.dao.contract.VehicleContract.COLUMN_VEHICLE_NO;
import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.*;
import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.TABLE_DEPARTMENT_STRING;

@Log4j
public class VehicleDaoImpl extends GenericDaoImpl<Vehicle> implements IVehicleDao {

    private static final String LOG_TAG = VehicleDaoImpl.class.getSimpleName();

    public static final List<String> projectionNameList = DatabaseConstant.combineMultipleList(VehicleContract.TABLE_COLUMN_NAME_LIST,
            VehicleTypeContract.TABLE_COLUMN_NAME_LIST);

    public VehicleDaoImpl(Connection connection) {
        super(connection, TABLE_VEHICLE_STRING);
    }

    @Override
    public Vehicle insert(Vehicle vehicle) throws SQLException {
        String[] columnArray = new String[]{COLUMN_VEHICLE_NO, VehicleContract.COLUMN_VEHICLE_TYPE_ID, COLUMN_CREATED_AT};
        String insertQuery = DatabaseConstant.createInsertRowQuery(tableName, columnArray);

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setString(1, vehicle.getVehicleNo());
        preparedStatement.setInt(2, vehicle.getVehicleType().getId());
        preparedStatement.setLong(3, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        int idGenerated = resultSet.getInt(1);
        //todo remove print
        System.out.println(idGenerated);
        vehicle.setId(idGenerated);
        resultSet.close();

        preparedStatement.close();

        return vehicle;
    }

    public int update(Vehicle vehicle) throws SQLException {
        String[] whereColumnName = {VehicleContract.COLUMN_ID};
        String[] valueUpdateColumnName = {COLUMN_VEHICLE_NO, VehicleContract.COLUMN_VEHICLE_TYPE_ID,COLUMN_UPDATED_AT};

        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createUpdateQuery(tableName, whereColumnName, valueUpdateColumnName));
        // update value
        preparedStatement.setString(1, vehicle.getVehicleNo());
        preparedStatement.setInt(2, vehicle.getVehicleType().getId());
        preparedStatement.setLong(3, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));
        // where condition value
        preparedStatement.setLong(4, vehicle.getId());

        int rowAffected = preparedStatement.executeUpdate();

        return rowAffected;
    }

    @Override
    public List<Vehicle> getAll() throws SQLException {
        String[] projectionNameArray = DatabaseConstant.convertListToArray(projectionNameList);
        String[] tableNameArray = {TABLE_VEHICLE_STRING, TABLE_VEHICLE_TYPE_STRING};
        String[] whereColumnNames = null;

        String query = DatabaseConstant.createGetRowByColumnNames(tableNameArray, projectionNameArray, null);
        query += " " + CONDITION_WHERE_STRING + " ";
        query += VehicleContract.TABLE_COLUMN_VEHICLE_TYPE_ID + " = " + VehicleTypeContract.TABLE_COLUMN_ID + ";";

        System.out.println(query);

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Vehicle> vehicleArrayList = new ArrayList<>();
        while (resultSet.next()) {
            vehicleArrayList.add(getRow(resultSet, projectionNameList));
        }

        return vehicleArrayList;
    }

    @Override
    public Vehicle getRow(ResultSet resultSet) throws SQLException {
        throw DatabaseConstant.getThrowsFunctionNotSupported(tableName, "getRow");
    }

    @Override
    public Vehicle getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException {

        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(resultSet.getInt(projectionNameList.indexOf(VehicleTypeContract.TABLE_COLUMN_ID) + 1));
        vehicleType.setType(resultSet.getString(projectionNameList.indexOf(VehicleTypeContract.TABLE_COLUMN_TYPE) + 1));

        Vehicle vehicle = new Vehicle();
        vehicle.setId(resultSet.getInt(projectionNameList.indexOf(VehicleContract.TABLE_COLUMN_ID) + 1));
        vehicle.setVehicleNo(resultSet.getString(projectionNameList.indexOf(VehicleContract.TABLE_COLUMN_VEHICLE_NO) + 1));
        vehicle.setVehicleType(vehicleType);

        return vehicle;
    }

    @Override
    public Vehicle getRowByName(String vehicleNo) throws SQLException {
        log.info("getRowByName, vehicleNo: " + vehicleNo);

        String[] projectionNameArray = DatabaseConstant.convertListToArray(projectionNameList);
        String[] tableNameArray = {TABLE_VEHICLE_STRING, TABLE_VEHICLE_TYPE_STRING};
        String[] whereColumnNames = {COLUMN_VEHICLE_NO};
        String query = DatabaseConstant.createGetRowByColumnNames(tableNameArray, projectionNameArray, whereColumnNames);
        query += " AND " + VehicleContract.TABLE_COLUMN_VEHICLE_TYPE_ID + " = " + VehicleTypeContract.TABLE_COLUMN_ID + ";";

        log.info(query);

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, vehicleNo);

        ResultSet resultSet = preparedStatement.executeQuery();

        Vehicle vehicle = null;
        if (resultSet.next()) {
            vehicle = getRow(resultSet, projectionNameList);
        }

        return vehicle;
    }
}
