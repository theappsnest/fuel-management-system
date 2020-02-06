package com.godavari.appsnest.fms.dao.concrete;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.DepartmentContract;
import com.godavari.appsnest.fms.dao.contract.HeadOfDepartmentContract;
import com.godavari.appsnest.fms.dao.interfaces.IDepartmentDao;
import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.godavari.appsnest.fms.dao.contract.BaseContract.*;
import static com.godavari.appsnest.fms.dao.contract.DepartmentContract.COLUMN_NAME;
import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.TABLE_DEPARTMENT_STRING;

public class DepartmentDaoImpl extends GenericDaoImpl<Department> implements IDepartmentDao {

    private static final String LOG_TAG = DepartmentDaoImpl.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(LOG_TAG);

    public DepartmentDaoImpl(Connection connection) {
        super(connection, TABLE_DEPARTMENT_STRING);
    }

    @Override
    public Department insert(Department department) throws SQLException {
        String[] columnArray = new String[]{COLUMN_NAME, COLUMN_CREATED_AT};
        String insertQuery = DatabaseConstant.createInsertRowQuery(tableName, columnArray);

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setString(1, department.getName());
        preparedStatement.setLong(2, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        int idGenerated = resultSet.getInt(1);
        department.setId(idGenerated);
        resultSet.close();

        preparedStatement.close();

        return department;
    }

    @Override
    public int update(Department department) throws SQLException
    {
        String[] whereColumnNames = {COLUMN_ID};
        String[] valueUpdateColumnName = {DepartmentContract.COLUMN_NAME, COLUMN_UPDATED_AT};

        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createUpdateQuery(tableName, whereColumnNames, valueUpdateColumnName));
        // update
        preparedStatement.setString(1, department.getName());
        preparedStatement.setLong(2, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));
        //where condition value
        preparedStatement.setInt(3, department.getId());

        int rowAffected = preparedStatement.executeUpdate();

        return rowAffected;
    }

    @Override
    public Department getRow(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt(resultSet.findColumn(COLUMN_ID)));
        department.setName(resultSet.getString(resultSet.findColumn(COLUMN_NAME)));
        return department;
    }

    @Override
    public Department getRowByName(String departmentName) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createGetRowByIdColumnName(tableName, null, COLUMN_NAME));
        preparedStatement.setString(1, departmentName);

        ResultSet resultSet = preparedStatement.executeQuery();

        Department department = null;
        while (resultSet.next()) {
            department = getRow(resultSet);
        }

        return department;
    }
}
