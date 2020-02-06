package com.godavari.appsnest.fms.dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IGenericDao<T> {
    T insert(T object) throws SQLException;

    int update(T object) throws SQLException;

    int delete(T object) throws SQLException;

    List<T> getAll() throws SQLException;

    int deleteAll() throws SQLException;

    T getRowById(int id) throws SQLException;

    T getRow(ResultSet resultSet) throws SQLException;

    T getRow(ResultSet resultSet, List<String> projectionNameList) throws SQLException;

    boolean isRowExistByStringColumn(String searchValue) throws SQLException;

}
