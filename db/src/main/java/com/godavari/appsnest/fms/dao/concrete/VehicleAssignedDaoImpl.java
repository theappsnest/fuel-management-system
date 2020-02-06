package com.godavari.appsnest.fms.dao.concrete;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.*;
import com.godavari.appsnest.fms.dao.interfaces.IVehicleAssignedDao;
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

import static com.godavari.appsnest.fms.dao.contract.HodManageContract.COLUMN_CURRENT;
import static com.godavari.appsnest.fms.dao.contract.VehicleAssignedContract.*;
import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.*;

@Log4j
public class VehicleAssignedDaoImpl extends GenericDaoImpl<VehicleAssigned> implements IVehicleAssignedDao {

    private static final String LOG_TAG = VehicleAssignedDaoImpl.class.getSimpleName();

    public VehicleAssignedDaoImpl(Connection connection) {
        super(connection, TABLE_VEHICLE_ASSIGNED_STRING);
    }

    @Override
    public VehicleAssigned insert(VehicleAssigned vehicleAssigned) throws SQLException {
        String[] columnArray = new String[]{COLUMN_DEPT_ID, COLUMN_VEHICLE_ID, COLUMN_CURRENT, COLUMN_CREATED_AT};
        String insertQuery = DatabaseConstant.createInsertRowQuery(tableName, columnArray);

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setInt(1, vehicleAssigned.getDepartment().getId());
        preparedStatement.setInt(2, vehicleAssigned.getVehicle().getId());
        preparedStatement.setBoolean(3, vehicleAssigned.isCurrent());
        preparedStatement.setLong(4, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        int idGenerated = resultSet.getInt(1);
        vehicleAssigned.setId(idGenerated);
        resultSet.close();

        preparedStatement.close();

        return vehicleAssigned;
    }

    @Override
    public int update(VehicleAssigned vehicleAssigned) throws SQLException {

        String[] whereColumnNames = {TABLE_COLUMN_ID};
        String[] valueUpdateColumnNames={COLUMN_DEPT_ID, COLUMN_VEHICLE_ID, VehicleAssignedContract.COLUMN_CURRENT,COLUMN_UPDATED_AT};

        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createUpdateQuery(tableName, whereColumnNames, valueUpdateColumnNames));
        // update value
        preparedStatement.setInt(1, vehicleAssigned.getDepartment().getId());
        preparedStatement.setInt(2, vehicleAssigned.getVehicle().getId());
        preparedStatement.setBoolean(3, vehicleAssigned.isCurrent());
        preparedStatement.setLong(4, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));
        // where condition value
        preparedStatement.setInt(5, vehicleAssigned.getId());

        int noRowAffected = preparedStatement.executeUpdate();

        return noRowAffected;
    }

    @Override
    public VehicleAssigned getRow(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    public List<VehicleAssigned> getAll() throws SQLException {
        log.info("getAll");
        List<String> projectionNameList = DatabaseConstant.combineMultipleList(VehicleAssignedContract.TABLE_COLUMN_NAME_LIST,
                DepartmentContract.TABLE_COLUMN_NAME_LIST, VehicleContract.TABLE_COLUMN_NAME_LIST, VehicleTypeContract.TABLE_COLUMN_NAME_LIST);

        String[] projectionNameArray = DatabaseConstant.convertListToArray(projectionNameList);
        String[] tableNameArray = {TABLE_VEHICLE_ASSIGNED_STRING, TABLE_DEPARTMENT_STRING, TABLE_VEHICLE_STRING, TABLE_VEHICLE_TYPE_STRING};
        String[] whereColumnNames = null;

        String query = DatabaseConstant.createGetRowByColumnNames(tableNameArray, projectionNameArray, null);
        if (whereColumnNames == null)
        {
            query+= " "+CONDITION_WHERE_STRING+" ";
        }
        else
        {
            query+=" AND ";
        }
        query += VehicleAssignedContract.TABLE_COLUMN_DEPT_ID + " = " + DepartmentContract.TABLE_COLUMN_ID;
        query += " AND " + VehicleAssignedContract.TABLE_COLUMN_VEHICLE_ID + " = " + VehicleContract.TABLE_COLUMN_ID ;
        query += " AND " + VehicleContract.TABLE_COLUMN_VEHICLE_TYPE_ID + " = " + VehicleTypeContract.TABLE_COLUMN_ID+";";
        System.out.println(query);

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<VehicleAssigned> vehicleAssignedList = new ArrayList<>();
        while (resultSet.next()) {
            vehicleAssignedList.add(getRow(resultSet, projectionNameList));
        }

        return vehicleAssignedList;
    }

    @Override
    public VehicleAssigned getRowById(int vehicleAssignedId) throws SQLException
    {
        log.info("getRowById, vehicleAssignedId: "+vehicleAssignedId);
        List<String> projectionNameList = DatabaseConstant.combineMultipleList(VehicleAssignedContract.TABLE_COLUMN_NAME_LIST,
                DepartmentContract.TABLE_COLUMN_NAME_LIST, VehicleContract.TABLE_COLUMN_NAME_LIST, VehicleTypeContract.TABLE_COLUMN_NAME_LIST);

        String[] projectionNameArray = DatabaseConstant.convertListToArray(projectionNameList);
        String[] tableNameArray = {TABLE_VEHICLE_ASSIGNED_STRING, TABLE_DEPARTMENT_STRING, TABLE_VEHICLE_STRING, TABLE_VEHICLE_TYPE_STRING};
        String[] whereColumnNames = {TABLE_COLUMN_ID};

        String query = DatabaseConstant.createGetRowByColumnNames(tableNameArray, projectionNameArray, whereColumnNames);
        query += " AND " +VehicleAssignedContract.TABLE_COLUMN_DEPT_ID + " = " + DepartmentContract.TABLE_COLUMN_ID;
        query += " AND " + VehicleAssignedContract.TABLE_COLUMN_VEHICLE_ID + " = " + VehicleContract.TABLE_COLUMN_ID ;
        query += " AND " + VehicleContract.TABLE_COLUMN_VEHICLE_TYPE_ID + " = " + VehicleTypeContract.TABLE_COLUMN_ID+";";
        System.out.println(query);

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,vehicleAssignedId);
        ResultSet resultSet = preparedStatement.executeQuery();

        VehicleAssigned vehicleAssigned = null;
        if (resultSet.next())
        {
            vehicleAssigned = getRow(resultSet, projectionNameList);
        }

        return  vehicleAssigned;
    }

    @Override
    public VehicleAssigned getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt(projectionNameList.indexOf(DepartmentContract.TABLE_COLUMN_ID) + 1));
        department.setName(resultSet.getString(projectionNameList.indexOf(DepartmentContract.TABLE_COLUMN_NAME) + 1));

        Vehicle vehicle = new Vehicle();
        vehicle.setId(resultSet.getInt(projectionNameList.indexOf(VehicleContract.TABLE_COLUMN_ID) + 1));
        vehicle.setVehicleNo(resultSet.getString(projectionNameList.indexOf(VehicleContract.TABLE_COLUMN_VEHICLE_NO) + 1));

        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(resultSet.getInt(projectionNameList.indexOf(VehicleTypeContract.TABLE_COLUMN_ID) + 1));
        vehicleType.setType(resultSet.getString(projectionNameList.indexOf(VehicleTypeContract.TABLE_COLUMN_TYPE) + 1));

        vehicle.setVehicleType(vehicleType);

        VehicleAssigned vehicleAssigned = new VehicleAssigned();
        vehicleAssigned.setId(resultSet.getInt(projectionNameList.indexOf(TABLE_COLUMN_ID) + 1));
        vehicleAssigned.setDepartment(department);
        vehicleAssigned.setVehicle(vehicle);
        vehicleAssigned.setCurrent(resultSet.getBoolean(projectionNameList.indexOf(TABLE_COLUMN_CURRENT) + 1));
        return vehicleAssigned;
    }

    @Override
    public List<VehicleAssigned> getAllCurrent(boolean current) throws SQLException {
        log.info("getAllCurrent, current: "+current);
        List<String> projectionNameList = DatabaseConstant.combineMultipleList(VehicleAssignedContract.TABLE_COLUMN_NAME_LIST,
                DepartmentContract.TABLE_COLUMN_NAME_LIST, VehicleContract.TABLE_COLUMN_NAME_LIST, VehicleTypeContract.TABLE_COLUMN_NAME_LIST);

        String[] projectionNameArray = DatabaseConstant.convertListToArray(projectionNameList);
        String[] tableNameArray = {TABLE_VEHICLE_ASSIGNED_STRING, TABLE_DEPARTMENT_STRING, TABLE_VEHICLE_STRING, TABLE_VEHICLE_TYPE_STRING};
        String[] whereColumnNames = {TABLE_COLUMN_CURRENT};

        String query = DatabaseConstant.createGetRowByColumnNames(tableNameArray, projectionNameArray, whereColumnNames);
        query += " AND " + VehicleAssignedContract.TABLE_COLUMN_DEPT_ID + " = " + DepartmentContract.TABLE_COLUMN_ID;
        query += " AND " + VehicleAssignedContract.TABLE_COLUMN_VEHICLE_ID + " = " + VehicleContract.TABLE_COLUMN_ID ;
        query += " AND " + VehicleContract.TABLE_COLUMN_VEHICLE_TYPE_ID + " = " + VehicleTypeContract.TABLE_COLUMN_ID+";";
        System.out.println(query);

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setBoolean(1,current);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<VehicleAssigned> vehicleAssignedList = new ArrayList<>();
        while (resultSet.next())
        {
            vehicleAssignedList.add(getRow(resultSet, projectionNameList));
        }

        return  vehicleAssignedList;
    }

    @Override
    public boolean isRowExistByVehicleIdAndCurrent(int vehicleId, boolean current) throws SQLException {

        String[] whereColumnNames = {COLUMN_VEHICLE_ID, COLUMN_CURRENT};
        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createIsRowExistQuery(tableName, whereColumnNames));
        preparedStatement.setInt(1, vehicleId);
        preparedStatement.setBoolean(2, current);

        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        int value = resultSet.getInt(1);
        if (value == 1)
            return true;
        else
            return false;
    }

    @Override
    public List<VehicleAssigned> getRowsByDepartmentIdAndCurrent(int departmentId, boolean current) throws SQLException {
        log.info("getRowsByDepartmentIdAndCurrent, departmentId: "+departmentId+", current"+current);
        List<String> projectionNameList = DatabaseConstant.combineMultipleList(VehicleAssignedContract.TABLE_COLUMN_NAME_LIST,
                DepartmentContract.TABLE_COLUMN_NAME_LIST, VehicleContract.TABLE_COLUMN_NAME_LIST, VehicleTypeContract.TABLE_COLUMN_NAME_LIST);

        String[] projectionNameArray = DatabaseConstant.convertListToArray(projectionNameList);
        String[] tableNameArray = {TABLE_VEHICLE_ASSIGNED_STRING, TABLE_DEPARTMENT_STRING, TABLE_VEHICLE_STRING, TABLE_VEHICLE_TYPE_STRING};
        String[] whereColumnNames = {VehicleAssignedContract.TABLE_COLUMN_DEPT_ID, TABLE_COLUMN_CURRENT};

        String query = DatabaseConstant.createGetRowByColumnNames(tableNameArray, projectionNameArray, whereColumnNames);
        query += " AND " + VehicleAssignedContract.TABLE_COLUMN_DEPT_ID + " = " + DepartmentContract.TABLE_COLUMN_ID;
        query += " AND " + VehicleAssignedContract.TABLE_COLUMN_VEHICLE_ID + " = " + VehicleContract.TABLE_COLUMN_ID ;
        query += " AND " + VehicleContract.TABLE_COLUMN_VEHICLE_TYPE_ID + " = " + VehicleTypeContract.TABLE_COLUMN_ID+";";
        System.out.println(query);

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,departmentId);
        preparedStatement.setBoolean(2,current);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<VehicleAssigned> vehicleAssignedList = new ArrayList<>();
        while (resultSet.next())
        {
            vehicleAssignedList.add(getRow(resultSet, projectionNameList));
        }

        return  vehicleAssignedList;
    }
}
