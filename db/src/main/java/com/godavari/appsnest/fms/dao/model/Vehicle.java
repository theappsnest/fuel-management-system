package com.godavari.appsnest.fms.dao.model;

import com.godavari.appsnest.fms.dao.abstracts.GenericModelOperationImpl;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.interfaces.IVehicleDao;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

/**
 *
 */

@Getter
@Setter
@Log4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Vehicle extends GenericModelOperationImpl<Vehicle> {

    private static final String LOG_TAG = Vehicle.class.getSimpleName();

    private static final String VEHICLE_VALIDATION_REGEX = "^[A-Z]{2}-[0-9]{1,2}-[A-Z]{1,3}-[0-9]{4}$";

    private int id;
    private String vehicleNo;
    private VehicleType vehicleType;

    public Vehicle(String vehicleNo, VehicleType vehicleType) {
        this.vehicleNo = vehicleNo;
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(LOG_TAG + "\n");
        stringBuilder.append("vehicleNo" + " : " + vehicleNo + "\n")
                .append("vehicleType" + " : " + vehicleType + "\n");

        return stringBuilder.toString();
    }

    public static boolean isValidVehicleNo(String vehicleNo) {
        if (!StringUtils.isEmpty(vehicleNo)) {
            return vehicleNo.matches(VEHICLE_VALIDATION_REGEX);
        }
        return false;
    }

    private static IVehicleDao getVehicleDao() {
        return DaoFactory.getDatabase().getVehicleDao();
    }

    @Override
    public Vehicle insert() throws SQLException {
        return getVehicleDao().insert(this);
    }

    public int update() throws SQLException {
        return getVehicleDao().update(this);
    }

    public static Vehicle getRowByName(String vehicleNo) throws SQLException {
        return getVehicleDao().getRowByName(vehicleNo);
    }

    public static List<Vehicle> getAll() throws SQLException {
        return getVehicleDao().getAll();
    }

    public static boolean isRowExistByName(String name) throws SQLException {
        return getVehicleDao().isRowExistByStringColumn(name);
    }
}
