package com.godavari.appsnest.fms.dao.concrete.report;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.AccountContract;
import com.godavari.appsnest.fms.dao.contract.HodManageContract;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.model.report.DepartmentAllModel;
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
public class ReportDepartmentsDaoImpl extends GenericDaoImpl<Object> {

    public ReportDepartmentsDaoImpl(Connection connection) {
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

    public DepartmentAllModel getRowByFromToDate(Department department, LocalDate fromLocalDate, LocalDate toLocalDate) throws SQLException {
        log.info("getRowByFromToDate, department: " + department + ", fromLocalDate: " + fromLocalDate + ", toLocalDate: " + toLocalDate);

        String query = "SELECT *, sum(" + AccountContract.COLUMN_OUT + ") OVER () AS total from " + DatabaseConstant.VIEW_REPORT_FROM_TO+
                " WHERE " + HodManageContract.COLUMN_DEPT_ID + " = ? " +
                " AND " + AccountContract.COLUMN_VEHICLE_ASSIGNED_ID + " IS NULL" +
                " AND " + AccountContract.COLUMN_DATE + " BETWEEN " + " ? " + " AND " + " ? ";

        System.out.println(query);

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, department.getId());
        preparedStatement.setLong(2, DatabaseConstant.localDateToEpoch(fromLocalDate));
        preparedStatement.setLong(3, DatabaseConstant.localDateToEpoch(toLocalDate));

        System.out.println(query + ", departmentId: " + department.getId() + ", " + department.getName() + ", from: " + DatabaseConstant.localDateToEpoch(fromLocalDate)
                + ", to: " + DatabaseConstant.localDateToEpoch(toLocalDate));

        ResultSet resultSet = preparedStatement.executeQuery();

        List<DepartmentVehicleRowModel> departmentVehicleRowModelList = new ArrayList<>();
        while (resultSet.next()) {
            departmentVehicleRowModelList.add((DepartmentVehicleRowModel) getRow(resultSet));
        }

        DepartmentAllModel departmentAllModel = new DepartmentAllModel();
        departmentAllModel.setDepartment(department);
        departmentAllModel.setDepartmentVehicleRowModelList(departmentVehicleRowModelList);

        return departmentAllModel;
    }

    @Override
    public Object getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException {
        return null;
    }
}
