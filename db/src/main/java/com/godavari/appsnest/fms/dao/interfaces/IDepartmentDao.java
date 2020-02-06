package com.godavari.appsnest.fms.dao.interfaces;

import com.godavari.appsnest.fms.dao.model.Department;

import java.sql.SQLException;

public interface IDepartmentDao extends IGenericDao<Department> {
    Department getRowByName(String departmentName)throws SQLException;
}
