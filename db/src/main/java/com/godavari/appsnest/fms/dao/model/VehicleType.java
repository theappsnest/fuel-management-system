package com.godavari.appsnest.fms.dao.model;

import com.godavari.appsnest.fms.dao.abstracts.GenericModelOperationImpl;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.interfaces.IVehicleTypeDao;
import com.godavari.appsnest.fms.dao.utility.UtilityMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Log4j
@EqualsAndHashCode(callSuper = false)
public class VehicleType extends GenericModelOperationImpl<VehicleType> {

    private static final String LOG_TAG = VehicleType.class.getSimpleName();

    private int id;
    private String type;

    public VehicleType(String type) {
        this.type = type;
    }

    @Override
    public void formatObject() {
        type = UtilityMethod.formatString(type);
    }

    @Override
    public VehicleType insert() throws SQLException {
        return getVehicleTypeDao().insert(this);
    }

    @Override
    public int delete() throws SQLException {
        return getVehicleTypeDao().delete(this);
    }

    @Override
    public int update() throws SQLException {
        return getVehicleTypeDao().update(this);
    }

    public static List<VehicleType> getAll() throws SQLException {
        return getVehicleTypeDao().getAll();
    }

    public static boolean isRowExistByName(String vehicleType) throws SQLException {
        return getVehicleTypeDao().isRowExistByStringColumn(vehicleType);
    }

    private static IVehicleTypeDao getVehicleTypeDao() {
        return DaoFactory.getDatabase().getVehicleTypeDao();
    }

    public static VehicleType getRowByVehicleType(String type) throws SQLException {
        return getVehicleTypeDao().getRowByType(type);
    }
}
