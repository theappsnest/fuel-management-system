package com.godavari.appsnest.fms.dao.concrete.report;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.AccountContract;
import com.godavari.appsnest.fms.dao.contract.VehicleAssignedContract;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.Vehicle;
import com.godavari.appsnest.fms.dao.model.report.VehicleAllModel;
import com.godavari.appsnest.fms.dao.model.report.DepartmentVehicleRowModel;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class ReportVehiclesDaoImpl extends GenericDaoImpl<Object> {

    public ReportVehiclesDaoImpl(Connection connection) {
        super(connection, null);
    }

    @Override
    public Object getRow(ResultSet resultSet) throws SQLException {

        DepartmentVehicleRowModel departmentVehicleRowModel = new DepartmentVehicleRowModel();

        Account account = (Account) DaoFactory.getDatabase().getReportFromToDao().getRow(resultSet, ReportFromToDaoImpl.projectionColumnNameList);
        departmentVehicleRowModel.setAccount(account);
        departmentVehicleRowModel.setTotal(resultSet.getDouble(resultSet.findColumn("total")));

        return departmentVehicleRowModel;
    }

    public VehicleAllModel getRowByFromToDate(Vehicle vehicle, LocalDate fromLocalDate, LocalDate toLocalDate) throws SQLException {
        log.info("getRowByFromToDate, vehicle: " + vehicle + ", fromLocalDate: " + fromLocalDate + ", toLocalDate: " + toLocalDate);

        String query = "SELECT *, sum(" + AccountContract.COLUMN_OUT + ") OVER () AS total from " + DatabaseConstant.VIEW_REPORT_FROM_TO +
                " WHERE " + VehicleAssignedContract.COLUMN_VEHICLE_ID + " = ? " +
                " AND " + AccountContract.COLUMN_DATE + " BETWEEN " + " ? " + " AND " + " ? ";

        System.out.println(query);

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, vehicle.getId());
        preparedStatement.setLong(2, DatabaseConstant.localDateToEpoch(fromLocalDate));
        preparedStatement.setLong(3, DatabaseConstant.localDateToEpoch(toLocalDate));

        System.out.println(query + ", vehicleId: " + vehicle.getId() + ", " + vehicle.getVehicleNo() + ", from: " + DatabaseConstant.localDateToEpoch(fromLocalDate)
                + ", to: " + DatabaseConstant.localDateToEpoch(toLocalDate));

        ResultSet resultSet = preparedStatement.executeQuery();

        List<DepartmentVehicleRowModel> departmentVehicleRowModelList = new ArrayList<>();
        while (resultSet.next()) {
            departmentVehicleRowModelList.add((DepartmentVehicleRowModel) getRow(resultSet));
        }

        VehicleAllModel vehicleAllModel = new VehicleAllModel();
        vehicleAllModel.setVehicle(vehicle);
        vehicleAllModel.setDepartmentVehicleRowModelList(departmentVehicleRowModelList);

        return vehicleAllModel;
    }

    @Override
    public Object getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException {
        return null;
    }
}
