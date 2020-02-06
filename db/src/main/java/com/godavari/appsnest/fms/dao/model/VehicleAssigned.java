package com.godavari.appsnest.fms.dao.model;

import com.godavari.appsnest.fms.dao.abstracts.GenericModelOperationImpl;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.interfaces.IVehicleAssignedDao;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;
import java.util.List;

/**
 *
 */

@Getter @Setter
@Log4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VehicleAssigned extends GenericModelOperationImpl<VehicleAssigned> {

    private static final String LOG_TAG = VehicleAssigned.class.getSimpleName();

    private int id;
    private Department department;
    private Vehicle vehicle;
    private boolean current=true;

    public VehicleAssigned(Department department, Vehicle vehicle) {
        this.department = department;
        this.vehicle = vehicle;
    }

    public String toString() {
        StringBuilder stringBuilder =new StringBuilder(LOG_TAG+"\n");
        stringBuilder.append("id"+" : "+id+"\n")
                .append("department"+" : "+department+"\n")
                .append("vehicle"+" : "+vehicle+"\n")
                .append("current"+" : "+current+"\n");

        return stringBuilder.toString();
    }

    private static IVehicleAssignedDao getVehicleAssignedDao()
    {
        return DaoFactory.getDatabase().getVehicleAssignedDao();
    }

    @Override
    public VehicleAssigned insert() throws SQLException {
        return getVehicleAssignedDao().insert(this);
    }

    @Override
    public int update() throws SQLException {
        return getVehicleAssignedDao().update(this);
    }

    public static List<VehicleAssigned> getAll() throws SQLException {
        return getVehicleAssignedDao().getAll();
    }

    public static List<VehicleAssigned> getAllByCurrent(boolean current) throws SQLException {
        return getVehicleAssignedDao().getAllCurrent(current);
    }

    public static VehicleAssigned getRowById(int vehicleAssignedId) throws SQLException
    {
        return getVehicleAssignedDao().getRowById(vehicleAssignedId);
    }

    public static boolean isRowExistByVehicleIdAndCurrent(int vehicleId, boolean current) throws SQLException
    {
        return getVehicleAssignedDao().isRowExistByVehicleIdAndCurrent(vehicleId, current);
    }

    public static List<VehicleAssigned> getRowByDepartmentIdAndCurrent(int departmentId, boolean current) throws SQLException
    {
        return getVehicleAssignedDao().getRowsByDepartmentIdAndCurrent(departmentId, current);
    }
}
