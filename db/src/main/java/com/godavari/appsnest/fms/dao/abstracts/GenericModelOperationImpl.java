package com.godavari.appsnest.fms.dao.abstracts;

import com.godavari.appsnest.fms.dao.interfaces.modeloperation.IGenericModelOperation;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;

import java.sql.SQLException;

public abstract class GenericModelOperationImpl<T> implements IGenericModelOperation<T> {

    public T insert() throws SQLException {
        throw DatabaseConstant.getThrowsFunctionNotSupported("Model Classes", "insert");
    }

    public int delete() throws SQLException {
        throw DatabaseConstant.getThrowsFunctionNotSupported("Model Classes", "delete");
    }

    public int update() throws SQLException {
        throw DatabaseConstant.getThrowsFunctionNotSupported("Model Classes", "update");
    }

    public abstract void formatObject();
}
