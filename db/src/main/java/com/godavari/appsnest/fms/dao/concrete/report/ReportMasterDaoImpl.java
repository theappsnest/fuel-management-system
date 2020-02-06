package com.godavari.appsnest.fms.dao.concrete.report;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.*;
import com.godavari.appsnest.fms.dao.model.report.MasterModel;
import com.godavari.appsnest.fms.dao.model.report.ReportMasterModel;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class ReportMasterDaoImpl extends GenericDaoImpl<Object> {

    public ReportMasterDaoImpl(Connection connection) {
        super(connection, null);
    }

    @Override
    public Object getRow(ResultSet resultSet) throws SQLException {
        MasterModel masterModel = new MasterModel();

        masterModel.setMonthYear(DatabaseConstant.epochToLocalDate(resultSet.getLong(resultSet.findColumn(ReportMasterContract.COLUMN_MONTH_YEAR))));
        masterModel.setOpeningStock(resultSet.getDouble(resultSet.findColumn(ReportMasterContract.COLUMN_MONTHLY_OPENING)));
        masterModel.setInput(resultSet.getDouble(resultSet.findColumn(ReportMasterContract.COLUMN_INPUT)));
        masterModel.setOutput(resultSet.getDouble(resultSet.findColumn(ReportMasterContract.COLUMN_OUTPUT)));
        masterModel.setClosingStock(resultSet.getDouble(resultSet.findColumn(ReportMasterContract.COLUMN_MONTHLY_CLOSING)));

        return masterModel;
    }

    public List<MasterModel> getRowByFromToDate(ReportMasterModel reportMasterModel) throws SQLException {
        log.info("getRowByFromToDate, reportMasterModel: " + reportMasterModel);

        String[] viewName = {DatabaseConstant.VIEW_REPORT_MASTER};
        String query = DatabaseConstant.createGetRowByColumnNames(viewName, null, null);
        query += " Where " + ReportMasterContract.COLUMN_MONTH_YEAR + " Between " + " ? " + " AND " + " ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setLong(1, DatabaseConstant.localDateToEpoch(reportMasterModel.getFromLocalDate()));
        preparedStatement.setLong(2, DatabaseConstant.localDateToEpoch(reportMasterModel.getToLocalDate()));

        ResultSet resultSet = preparedStatement.executeQuery();

        List<MasterModel> masterModelList = new ArrayList<>();
        while (resultSet.next()) {
            masterModelList.add((MasterModel) getRow(resultSet));
        }

        reportMasterModel.setMasterModelList(masterModelList);

        return masterModelList;
    }

    @Override
    public Object getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException {

        return null;
    }
}
