package com.godavari.appsnest.fms.dao.interfaces;

import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;

import java.sql.SQLException;

public interface IHeadOfDepartmentDao extends IGenericDao<HeadOfDepartment> {
    HeadOfDepartment getRowByHodName(String hodName) throws SQLException;
}
