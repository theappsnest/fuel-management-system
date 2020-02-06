package com.godavari.appsnest.fms.dao.interfaces;

import com.godavari.appsnest.fms.dao.model.Vehicle;

import java.sql.SQLException;

public interface IVehicleDao extends IGenericDao<Vehicle> {
    Vehicle getRowByName(String vehicleNO)throws SQLException;
}
