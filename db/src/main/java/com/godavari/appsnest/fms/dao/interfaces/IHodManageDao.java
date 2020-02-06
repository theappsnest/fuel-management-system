package com.godavari.appsnest.fms.dao.interfaces;

import com.godavari.appsnest.fms.dao.model.HodManage;

import java.sql.SQLException;
import java.util.List;

public interface IHodManageDao extends IGenericDao<HodManage> {
    List<HodManage> getAllCurrent(boolean current) throws SQLException;

    boolean isRowExistByDeptIdAndCurrent(int departmentId, boolean current) throws SQLException;
}
