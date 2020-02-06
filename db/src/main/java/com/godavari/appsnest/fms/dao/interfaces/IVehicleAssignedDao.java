package com.godavari.appsnest.fms.dao.interfaces;

import com.godavari.appsnest.fms.dao.model.VehicleAssigned;

import java.sql.SQLException;
import java.util.List;

public interface IVehicleAssignedDao extends IGenericDao<VehicleAssigned> {
    List<VehicleAssigned> getAllCurrent(boolean current) throws SQLException;
    boolean isRowExistByVehicleIdAndCurrent(int vehicleId, boolean current) throws SQLException;
    List<VehicleAssigned> getRowsByDepartmentIdAndCurrent(int departmentId, boolean current) throws SQLException;
}
