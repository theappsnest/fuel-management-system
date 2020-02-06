package com.godavari.appsnest.fms.dao.interfaces;

import com.godavari.appsnest.fms.dao.model.VehicleType;

import java.sql.SQLException;

public interface IVehicleTypeDao extends IGenericDao<VehicleType> {
    VehicleType getRowByType(String vehicleType) throws SQLException;
}
