package com.godavari.appsnest.fms.dao.concrete;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.HeadOfDepartmentContract;
import com.godavari.appsnest.fms.dao.interfaces.IHeadOfDepartmentDao;
import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.godavari.appsnest.fms.dao.contract.BaseContract.*;
import static com.godavari.appsnest.fms.dao.contract.HeadOfDepartmentContract.COLUMN_NAME;
import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.TABLE_HEAD_OF_DEPARTMENT_STRING;

public class HeadOfDepartmentDaoImpl extends GenericDaoImpl<HeadOfDepartment> implements IHeadOfDepartmentDao {

    private static final String LOG_TAG = HeadOfDepartmentDaoImpl.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(LOG_TAG);

    public HeadOfDepartmentDaoImpl(Connection connection) {
        super(connection, TABLE_HEAD_OF_DEPARTMENT_STRING);
    }

    @Override
    public HeadOfDepartment insert(HeadOfDepartment headOfDepartment) throws SQLException {
        String[] columnArray = new String[]{COLUMN_NAME, COLUMN_CREATED_AT};
        String insertQuery = DatabaseConstant.createInsertRowQuery(tableName, columnArray);

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setString(1, headOfDepartment.getName());
        preparedStatement.setLong(2, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        int idGenerated = resultSet.getInt(1);
        headOfDepartment.setId(idGenerated);
        resultSet.close();

        preparedStatement.close();

        return headOfDepartment;
    }

    @Override
    public int update(HeadOfDepartment headOfDepartment) throws SQLException {
        String[] whereColumnNames = {COLUMN_ID};
        String[] valueUpdateColumnName = {COLUMN_NAME, COLUMN_UPDATED_AT};

        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createUpdateQuery(tableName, whereColumnNames, valueUpdateColumnName));
        // update
        preparedStatement.setString(1, headOfDepartment.getName());
        preparedStatement.setLong(2, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));
        //where condition value
        preparedStatement.setInt(3, headOfDepartment.getId());

        int rowAffected = preparedStatement.executeUpdate();

        return rowAffected;
    }

    @Override
    public HeadOfDepartment getRow(ResultSet resultSet) throws SQLException {
        HeadOfDepartment row = new HeadOfDepartment();
        row.setId(resultSet.getInt(resultSet.findColumn(COLUMN_ID)));
        row.setName(resultSet.getString(resultSet.findColumn(COLUMN_NAME)));
        return row;
    }

    @Override
    public HeadOfDepartment getRowByHodName(String hodName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createGetRowByIdColumnName(tableName, null, COLUMN_NAME));
        preparedStatement.setString(1, hodName);
        ResultSet resultSet=preparedStatement.executeQuery();

        HeadOfDepartment headOfDepartment = null;
        if (resultSet.next())
        {
            headOfDepartment = getRow(resultSet);
        }
        return headOfDepartment;
    }
}
