package com.godavari.appsnest.fms.dao.daofactory;

import com.godavari.appsnest.fms.dao.concrete.report.*;
import com.godavari.appsnest.fms.dao.interfaces.*;
import org.apache.log4j.Logger;

import java.sql.Connection;

public abstract class DaoFactory {

    private static final String LOG_TAG = DaoFactory.class.getSimpleName();
     private static final Logger logger = Logger.getLogger(LOG_TAG);
    /*
     * There will be a method for each DAO that can be
     * created. The concrete factories will have to
     * implement these methods.
     */

    public abstract Connection openConnection();

    public abstract IAccountDao getAccountDao();
    public abstract IDepartmentDao getDepartmentDao();
    public abstract IHeadOfDepartmentDao getHeadOfDepartmentDao();
    public abstract IHodManageDao getHodManageDao();
    public abstract IVehicleDao getVehicleDao();
    public abstract IVehicleAssignedDao getVehicleAssignedDao();
    public abstract IVehicleTypeDao getVehicleTypeDao();
    public abstract ReportFromToDaoImpl getReportFromToDao();
    public abstract ReportMasterDaoImpl getReportMasterDao();
    public abstract ReportMaster1DaoImpl getReportMaster1Dao();
    public abstract ReportVehiclesDaoImpl getReportVehiclesDao();
    public abstract ReportDepartmentsDaoImpl getReportDepartmentsDao();
    public abstract IMD5ModelDao getMD5ModelDao();
    public abstract ILoginModelDao getLoginModelDao();

    public static DaoFactory getDatabase() {
        return DbSqlite.getDatabase();
    }
}
