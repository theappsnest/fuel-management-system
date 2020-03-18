package com.godavari.appsnest.fms.dao.model;

import com.godavari.appsnest.fms.dao.abstracts.GenericModelOperationImpl;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.interfaces.IHodManageDao;
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
public class HodManage extends GenericModelOperationImpl<HodManage> {

    private static final String LOG_TAG = HodManage.class.getSimpleName();

    private int id;
    private Department department;
    private HeadOfDepartment headOfDepartment;
    private boolean current = true;

    public HodManage(Department department, HeadOfDepartment headOfDepartment) {
        this.department = department;
        this.headOfDepartment = headOfDepartment;
    }

    @Override
    public void formatObject() {
        if (department != null) {
            department.formatObject();
        }
        if (headOfDepartment != null) {
            headOfDepartment.formatObject();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(LOG_TAG + "\n");
        stringBuilder.append("id" + " : " + id + "\n")
                .append("department" + " : " + department + "\n")
                .append("headOfDepartment" + " : " + headOfDepartment + "\n")
                .append("current" + " : " + current + "\n");

        return stringBuilder.toString();
    }

    private static IHodManageDao getHodManageDao() {
        return DaoFactory.getDatabase().getHodManageDao();
    }

    @Override
    public HodManage insert() throws SQLException {
        return getHodManageDao().insert(this);
    }

    @Override
    public int delete() throws SQLException {
        return getHodManageDao().delete(this);
    }

    @Override
    public int update() throws SQLException {
        return getHodManageDao().update(this);
    }

    public static List<HodManage> getAll() throws SQLException {
        return getHodManageDao().getAll();
    }

    public static List<HodManage> getAllByCurrent(boolean current) throws SQLException {
        return getHodManageDao().getAllCurrent(current);
    }

    public static HodManage getRowById(int hodManageId) throws SQLException {
        return getHodManageDao().getRowById(hodManageId);
    }

    public static boolean isRowExistByDeptIdAndCurrent(int departmentId, boolean current) throws SQLException {
        return getHodManageDao().isRowExistByDeptIdAndCurrent(departmentId, current);
    }
}
