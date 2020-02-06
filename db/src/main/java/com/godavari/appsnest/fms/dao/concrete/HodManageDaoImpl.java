package com.godavari.appsnest.fms.dao.concrete;

import com.godavari.appsnest.fms.dao.abstracts.GenericDaoImpl;
import com.godavari.appsnest.fms.dao.contract.DepartmentContract;
import com.godavari.appsnest.fms.dao.contract.HeadOfDepartmentContract;
import com.godavari.appsnest.fms.dao.contract.HodManageContract;
import com.godavari.appsnest.fms.dao.interfaces.IHodManageDao;
import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;
import com.godavari.appsnest.fms.dao.model.HodManage;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.contract.HodManageContract.*;
import static com.godavari.appsnest.fms.dao.utility.DatabaseConstant.*;

@Log4j
public class HodManageDaoImpl extends GenericDaoImpl<HodManage> implements IHodManageDao {

    private static final String LOG_TAG = HodManageDaoImpl.class.getSimpleName();

    public HodManageDaoImpl(Connection connection) {
        super(connection, TABLE_HOD_MANAGE_STRING);
    }

    @Override
    public List<HodManage> getAll() throws SQLException {
        log.info("getAll");
        String query = "SELECT hod_manage.id, department.id, department.name, hod.id, hod.name, hod_manage.current\n" +
                " FROM hod_manage, department, hod\n" +
                " WHERE hod_manage.dept_id == department.id AND hod_manage.hod_id == hod.id;";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        List<HodManage> hodManages = new ArrayList<>();

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            hodManages.add(getRow(resultSet));
        }

        return hodManages;
    }

    @Override
    public HodManage insert(HodManage hodManage) throws SQLException {
        String[] columnArray = new String[]{COLUMN_DEPT_ID, COLUMN_HOD_ID, COLUMN_CURRENT, COLUMN_CREATED_AT};
        String insertQuery = DatabaseConstant.createInsertRowQuery(tableName, columnArray);

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setInt(1, hodManage.getDepartment().getId());
        preparedStatement.setInt(2, hodManage.getHeadOfDepartment().getId());
        preparedStatement.setBoolean(3, hodManage.isCurrent());
        preparedStatement.setLong(4, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        int idGenerated = resultSet.getInt(1);
        hodManage.setId(idGenerated);
        resultSet.close();

        preparedStatement.close();

        return hodManage;
    }

    @Override
    public int update(HodManage hodManage) throws SQLException {
        String[] whereColumnNames = {COLUMN_ID};
        String[] valueUpdateColumnName = {COLUMN_DEPT_ID, COLUMN_HOD_ID, COLUMN_CURRENT, COLUMN_UPDATED_AT};

        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseConstant.createUpdateQuery(tableName, whereColumnNames, valueUpdateColumnName));
        // update
        preparedStatement.setInt(1, hodManage.getDepartment().getId());
        preparedStatement.setInt(2, hodManage.getHeadOfDepartment().getId());
        preparedStatement.setBoolean(3, hodManage.isCurrent());
        preparedStatement.setLong(4, DatabaseConstant.localDateTimeToEpoch(LocalDateTime.now()));
        //where condition value
        preparedStatement.setInt(5, hodManage.getId());

        int rowAffected = preparedStatement.executeUpdate();

        return rowAffected;
    }

    @Override
    public HodManage getRow(ResultSet resultSet) throws SQLException {
        //SELECT hod_manage.id, department.id, department.name, hod.id, hod.name, hod_manage.current

        Department department = new Department();
        department.setId(resultSet.getInt(2));
        department.setName(resultSet.getString(3));

        HeadOfDepartment headOfDepartment = new HeadOfDepartment();
        headOfDepartment.setId(resultSet.getInt(4));
        headOfDepartment.setName(resultSet.getString(5));

        HodManage hodManage = new HodManage();
        hodManage.setId(resultSet.getInt(1));
        hodManage.setDepartment(department);
        hodManage.setHeadOfDepartment(headOfDepartment);
        hodManage.setCurrent(resultSet.getBoolean(6));

        return hodManage;
    }

    @Override
    public HodManage getRow(ResultSet resultSet, List<String> projectNameList) throws SQLException {

        Department department = new Department();
        department.setId(resultSet.getInt(projectNameList.indexOf(DepartmentContract.TABLE_COLUMN_ID) + 1));
        department.setName(resultSet.getString(projectNameList.indexOf(DepartmentContract.TABLE_COLUMN_NAME) + 1));

        HeadOfDepartment headOfDepartment = new HeadOfDepartment();
        headOfDepartment.setId(resultSet.getInt(projectNameList.indexOf(HeadOfDepartmentContract.TABLE_COLUMN_ID) + 1));
        headOfDepartment.setName(resultSet.getString(projectNameList.indexOf(HeadOfDepartmentContract.TABLE_COLUMN_NAME) + 1));

        HodManage hodManage = new HodManage();
        hodManage.setId(resultSet.getInt(projectNameList.indexOf(HodManageContract.TABLE_COLUMN_ID) + 1));
        hodManage.setDepartment(department);
        hodManage.setHeadOfDepartment(headOfDepartment);
        hodManage.setCurrent(resultSet.getBoolean(projectNameList.indexOf(TABLE_COLUMN_CURRENT) + 1));

        return hodManage;
    }

    @Override
    public List<HodManage> getAllCurrent(boolean current) throws SQLException {
        log.info("getAllCurrent, current: " + current);

        List<String> projectionNameList = DatabaseConstant.combineMultipleList(TABLE_COLUMN_NAME_LIST,
                DepartmentContract.TABLE_COLUMN_NAME_LIST, HeadOfDepartmentContract.TABLE_COLUMN_NAME_LIST);

        String[] projectionNameArray = DatabaseConstant.convertListToArray(projectionNameList);
        String[] tableNameArray = {TABLE_HOD_MANAGE_STRING, TABLE_DEPARTMENT_STRING, TABLE_HEAD_OF_DEPARTMENT_STRING};
        String[] whereColumnNames = {HodManageContract.TABLE_COLUMN_CURRENT};

        String query = DatabaseConstant.createGetRowByColumnNames(tableNameArray, projectionNameArray, whereColumnNames);
        query += " AND " + HodManageContract.TABLE_COLUMN_DEPT_ID + " = " + DepartmentContract.TABLE_COLUMN_ID;
        query += " AND " + HodManageContract.TABLE_COLUMN_HOD_ID + " = " + HeadOfDepartmentContract.TABLE_COLUMN_ID + ";";

        System.out.println(query);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setBoolean(1, current);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<HodManage> hodManageList = new ArrayList<>();
        while (resultSet.next()) {
            hodManageList.add(getRow(resultSet, projectionNameList));
        }

        return hodManageList;
    }

    @Override
    public boolean isRowExistByDeptIdAndCurrent(int departmentId, boolean current) throws SQLException {
        String[] whereColumnNames = new String[]{COLUMN_DEPT_ID, COLUMN_CURRENT};

        String query = DatabaseConstant.createIsRowExistQuery(tableName, whereColumnNames);

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, departmentId);
        preparedStatement.setBoolean(2, current);

        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        int value = resultSet.getInt(1);
        if (value == 1)
            return true;
        else
            return false;
    }

    @Override
    public HodManage getRowById(int hodManageId) throws SQLException {
        log.info("getRowById, hodManageId: " + hodManageId);
        List<String> projectionNameList = DatabaseConstant.combineMultipleList(TABLE_COLUMN_NAME_LIST,
                DepartmentContract.TABLE_COLUMN_NAME_LIST, HeadOfDepartmentContract.TABLE_COLUMN_NAME_LIST);

        String[] projectionNameArray = DatabaseConstant.convertListToArray(projectionNameList);
        String[] tableNameArray = {TABLE_HOD_MANAGE_STRING, TABLE_DEPARTMENT_STRING, TABLE_HEAD_OF_DEPARTMENT_STRING};
        String[] whereColumnNames = {HodManageContract.TABLE_COLUMN_ID};

        String query = DatabaseConstant.createGetRowByColumnNames(tableNameArray, projectionNameArray, whereColumnNames);
        query += " AND " + HodManageContract.TABLE_COLUMN_DEPT_ID + " = " + DepartmentContract.TABLE_COLUMN_ID;
        query += " AND " + HodManageContract.TABLE_COLUMN_HOD_ID + " = " + HeadOfDepartmentContract.TABLE_COLUMN_ID + ";";

        System.out.println(query);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, hodManageId);
        ResultSet resultSet = preparedStatement.executeQuery();

        HodManage hodManage = null;
        if (resultSet.next()) {
            hodManage = getRow(resultSet, projectionNameList);
        }

        return hodManage;
    }
}
