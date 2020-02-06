package com.godavari.appsnest.fms.dao.interfaces.modeloperation;

import java.sql.SQLException;

public interface IGenericModelOperation<T> {
    T insert() throws SQLException;

    int delete() throws SQLException;

    int update() throws SQLException;
}
