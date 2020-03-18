package com.godavari.appsnest.fms.dao.model;

import com.godavari.appsnest.fms.dao.abstracts.GenericModelOperationImpl;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.interfaces.IDepartmentDao;
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
public class Department extends GenericModelOperationImpl<Department> {

    private static final String LOG_TAG = Department.class.getSimpleName();

    private int id;
    private String name;

    public Department(String name) {
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
                .append("name" + " : " + name + "\n");

        return stringBuilder.toString();
    }

    private static IDepartmentDao getDepartmentDao() {
        return DaoFactory.getDatabase().getDepartmentDao();
    }

    @Override
    public Department insert() throws SQLException {
        return getDepartmentDao().insert(this);
    }

    @Override
    public int update() throws SQLException {
        return getDepartmentDao().update(this);
    }

    @Override
    public int delete() throws SQLException {
        return getDepartmentDao().delete(this);
    }

    public static List<Department> getAll() throws SQLException {
        return getDepartmentDao().getAll();
    }

    public static Department getRowByName(String departmentName) throws SQLException {
        return getDepartmentDao().getRowByName(departmentName);
    }

    public static boolean isRowExistByName(String departmentName) throws SQLException {
        return getDepartmentDao().isRowExistByStringColumn(departmentName);
    }

    public static void main(String[] args) {
        try {
            Department department = new Department();
            department.setId(1);
            department.setName("cs");
            department.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
