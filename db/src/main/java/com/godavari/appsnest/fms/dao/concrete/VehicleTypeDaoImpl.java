package com.godavari.appsnest.fms.dao.concrete;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.VehicleTypeContract;
import com.godavari.appsnest.fms.dao.interfaces.IVehicleTypeDao;
import com.godavari.appsnest.fms.dao.model.VehicleType;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.godavari.appsnest.fms.dao.contract.BaseContract.*;
import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.TABLE_VEHICLE_TYPE_STRING;

@Log4j
public class VehicleTypeDaoImpl extends GenericDaoImpl<VehicleType> implements IVehicleTypeDao {

    public VehicleTypeDaoImpl(Connection connection) {
        super(connection, TABLE_VEHICLE_TYPE_STRING);
    }

    @Override
    public VehicleType insert(VehicleType vehicleType) throws SQLException {
        String[] columnArray = new String[]{VehicleTypeContract.COLUMN_TYPE, COLUMN_CREATED_AT};
        String insertQuery = DatabaseConstant.createInsertRowQuery(tableName, columnArray);

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setString(1, vehicleType.getType());
        preparedStatement.setLong(2, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        int idGenerated = resultSet.getInt(1);
        vehicleType.setId(idGenerated);
        resultSet.close();

        preparedStatement.close();

        return vehicleType;
    }

    @Override
    public int update(VehicleType vehicleType) throws SQLException {
        String[] whereColumnNames = {COLUMN_ID};
        String[] valueUpdateColumnName = {VehicleTypeContract.COLUMN_TYPE,COLUMN_UPDATED_AT};

        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createUpdateQuery(tableName, whereColumnNames, valueUpdateColumnName));
        // update
        preparedStatement.setString(1, vehicleType.getType());
        preparedStatement.setLong(2, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));
        //where condition value
        preparedStatement.setInt(3, vehicleType.getId());

        int rowAffected = preparedStatement.executeUpdate();

        return rowAffected;
    }

    @Override
    public VehicleType getRow(ResultSet resultSet) throws SQLException {
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(resultSet.getInt(resultSet.findColumn(VehicleTypeContract.COLUMN_ID)));
        vehicleType.setType(resultSet.getString(resultSet.findColumn(VehicleTypeContract.COLUMN_TYPE)));
        return vehicleType;
    }

    @Override
    public VehicleType getRowByType(String type) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createGetRowByIdColumnName(tableName, null, VehicleTypeContract.COLUMN_TYPE));
        preparedStatement.setString(1, type);

        ResultSet resultSet = preparedStatement.executeQuery();

        VehicleType vehicleType = null;
        if (resultSet.next()) {
            vehicleType = getRow(resultSet);
        }
        return vehicleType;
    }
}
