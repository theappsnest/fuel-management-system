package com.godavari.appsnest.fms.dao.concrete.report;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.DepartmentContract;
import com.godavari.appsnest.fms.dao.contract.VehicleContract;
import com.godavari.appsnest.fms.dao.model.report.Master1Model;
import com.godavari.appsnest.fms.dao.model.report.ReportMaster1Model;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class ReportMaster1DaoImpl extends GenericDaoImpl<Object> {

    public ReportMaster1DaoImpl(Connection connection) {
        super(connection, null);
    }

    @Override
    public Object getRow(ResultSet resultSet) throws SQLException {
        return null;
    }

    private Object getRow(ResultSet resultSet, ReportMaster1Model reportMaster1Model) throws SQLException {
        Master1Model master1Model = new Master1Model();

        master1Model.setDepartmentName(resultSet.getString(resultSet.findColumn(DepartmentContract.COLUMN_NAME)));
        master1Model.setVehicleNo(resultSet.getString(resultSet.findColumn(VehicleContract.COLUMN_VEHICLE_NO)));

        List<Double> monthlyTotal = new ArrayList<>();
        int diffMonth = DatabaseConstant.differenceBetweenDates(reportMaster1Model.getFromLocalDate(), reportMaster1Model.getToLocalDate());
        for (int i = 0; i <= diffMonth; i++) {
            monthlyTotal.add(resultSet.getDouble(resultSet.findColumn(getMonthYear(reportMaster1Model.getFromLocalDate().plusMonths(i)))));
        }

        master1Model.setMonthlyConsumption(monthlyTotal);
        master1Model.formatObject();
        return master1Model;
    }

    public List<Master1Model> getRowByFromToDate(ReportMaster1Model reportMaster1Model) throws SQLException {
        log.info("getRowByFromToDate, reportMasterModel: " + reportMaster1Model);

        String query = "select name, vehicle_no, ";
        int diffMonth = DatabaseConstant.differenceBetweenDates(reportMaster1Model.getFromLocalDate(), reportMaster1Model.getToLocalDate());
        System.out.println(diffMonth);
        for (int i = 0; i < diffMonth; i++) {
            query += getSumCaseExpression(reportMaster1Model.getFromLocalDate().plusMonths(i), "date", "output") + ", ";
        }
        query += getSumCaseExpression(reportMaster1Model.getFromLocalDate().plusMonths(diffMonth), "date", "output") + " ";

        query += "FROM " + DatabaseConstant.VIEW_REPORT_FROM_TO + " group by name, vehicle_no ORDER by  name ASC, vehicle_no ASC";

        System.out.println(query);

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<Master1Model> master1ModelList = new ArrayList<>();
        while (resultSet.next()) {
            master1ModelList.add((Master1Model) getRow(resultSet, reportMaster1Model));
        }

        reportMaster1Model.setMaster1ModelList(master1ModelList);

        return master1ModelList;
    }

    @Override
    public Object getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException {

        return null;
    }

    private String getSumCaseExpression(LocalDate localDate, String dateColumnName, String outputColumnName) {
        //"SUM(CASE WHEN strftime('%m %Y', datetime(date, 'unixepoch')) = '01 2020' THEN output END) AS 'JAN'

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SUM ( CASE WHEN ")
                .append(DatabaseConstant.strftimeFormat("%m %Y", dateColumnName) + " = \"" + getMonthYear(localDate) + "\" ")
                .append(" THEN " + outputColumnName + " END) ")
                .append(" AS \"" + getMonthYear(localDate) + "\"");
        return stringBuilder.toString();
    }

    public static String getMonthYear(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return localDate.format(formatter) + " " + localDate.getYear();
    }

}
