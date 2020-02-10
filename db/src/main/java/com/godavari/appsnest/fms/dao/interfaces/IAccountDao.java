package com.godavari.appsnest.fms.dao.interfaces;

import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.Vehicle;

import java.sql.SQLException;
import java.time.LocalDateTime;

public interface IAccountDao extends IGenericDao<Account> {
    Account getAccountByVehicleAndDateTime(Vehicle vehicle, LocalDateTime currentSelectedLocalDateTime) throws SQLException;
}
