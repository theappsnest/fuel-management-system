package com.godavari.appsnest.fms.dao.model;

import com.godavari.appsnest.fms.dao.abstracts.GenericModelOperationImpl;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.interfaces.IHeadOfDepartmentDao;
import com.godavari.appsnest.fms.dao.utility.UtilityMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;
import java.util.List;

/**
 *
 */

@Getter
@Setter
@Log4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HeadOfDepartment extends GenericModelOperationImpl<HeadOfDepartment> {

    private static final String LOG_TAG = HeadOfDepartment.class.getSimpleName();

    private int id;
    private String name;

    public HeadOfDepartment(String name) {
        this.name = name;
    }

    @Override
    public void formatObject() {
        name = UtilityMethod.formatString(name);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(LOG_TAG + "\n");
        stringBuilder.append("id" + " : " + id + "\n")
                .append("firstName" + " : " + name + "\n");

        return stringBuilder.toString();
    }

    private static IHeadOfDepartmentDao getHeadOfDepartmentDao() {
        return DaoFactory.getDatabase().getHeadOfDepartmentDao();
    }

    @Override
    public HeadOfDepartment insert() throws SQLException {
        return getHeadOfDepartmentDao().insert(this);
    }

    @Override
    public int delete() throws SQLException {
        return getHeadOfDepartmentDao().delete(this);
    }

    @Override
    public int update() throws SQLException
    {
        return getHeadOfDepartmentDao().update(this);
    }
    public static List<HeadOfDepartment> getAll() throws SQLException {
        return getHeadOfDepartmentDao().getAll();
    }

    public static boolean isRowExistByName(String name) throws SQLException {
        return getHeadOfDepartmentDao().isRowExistByStringColumn(name);
    }

    public static HeadOfDepartment getRowByHodName(String name) throws SQLException
    {
        return getHeadOfDepartmentDao().getRowByHodName(name);
    }
}
