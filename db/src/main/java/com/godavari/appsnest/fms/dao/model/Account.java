package com.godavari.appsnest.fms.dao.model;

import com.godavari.appsnest.fms.dao.abstracts.GenericModelOperationImpl;
import com.godavari.appsnest.fms.dao.concrete.AccountDaoImpl;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.interfaces.IAccountDao;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */

@Getter
@Setter
@Log4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Account extends GenericModelOperationImpl<Account> {

    private static final String LOG_TAG = Account.class.getSimpleName();

    private int id;
    private LocalDateTime dateTime;
    private HodManage hodManage;
    private VehicleAssigned vehicleAssigned;
    private double currentReading;
    private double input;
    private double output;
    private String owner;
    private double mileageKmPerHour;

    public Account(HodManage hodManage, VehicleAssigned vehicleAssigned, LocalDateTime dateTime, double currentReading, double input, double output, String owner
            , float mileageKmPerHour) {
        this.hodManage = hodManage;
        this.vehicleAssigned = vehicleAssigned;
        this.currentReading = currentReading;
        this.dateTime = dateTime;
        this.input = input;
        this.output = output;
        this.owner = owner;
        this.mileageKmPerHour = mileageKmPerHour;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(LOG_TAG + "\n");
        stringBuilder.append("id" + " : " + id + "\n")
                .append("hodManage" + " : " + hodManage + "\n")
                .append("vehicleAssigned" + " : " + vehicleAssigned + "\n")
                .append("dateTime" + " : " + dateTime + "\n")
                .append("currentReading" + " : " + currentReading + "\n")
                .append("in" + " : " + input + "\n")
                .append("out" + " : " + output + "\n")
                .append("owner" + " : " + owner + "\n")
                .append("mileageKmPerHour" + " : " + mileageKmPerHour + "\n");

        return stringBuilder.toString();
    }

    private static IAccountDao getAccountDao() {
        return DaoFactory.getDatabase().getAccountDao();
    }

    @Override
    public Account insert() throws SQLException {
        return getAccountDao().insert(this);
    }

    @Override
    public int update() throws SQLException {
        return getAccountDao().update(this);
    }

    public static Account getAccountByVehicleAndDateTime(Vehicle vehicleSelected,
                                                         LocalDateTime currentSelectedLocalDateTime) throws SQLException {
        return getAccountDao().getAccountByVehicleAndDateTime(vehicleSelected, currentSelectedLocalDateTime);
    }

    public static List<Account> getAll() throws SQLException {
        return getAccountDao().getAll();
    }

    @Override
    public int delete() throws SQLException {
        return getAccountDao().delete(this);
    }
}
