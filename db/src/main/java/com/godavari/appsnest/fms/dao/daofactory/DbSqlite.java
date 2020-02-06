package com.godavari.appsnest.fms.dao.daofactory;

import com.godavari.appsnest.fms.dao.concrete.*;
import com.godavari.appsnest.fms.dao.concrete.report.*;
import com.godavari.appsnest.fms.dao.interfaces.*;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbSqlite extends DaoFactory {

    private static final String LOG_TAG = DbSqlite.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(LOG_TAG);

    private static final String DATABASE_NAME = "database.db";
    private static final String DATABASE_URL = "jdbc:sqlite:" + "database/" + DATABASE_NAME;

    private static final String FILE_GENERATED_PARENT_FOLDER_NAME = "file generated";
    private static final String MD5_MODEL_GENERATED_FOLDER_NAME = "general information";
    private static final String LOGIN_MODEL_GENERATED_FOLDER_NAME = "other information";

    private Connection connection;

    private static DbSqlite dbSqlite;

    private DbSqlite() {
    }

    public synchronized static DaoFactory getDatabase() {
        if (dbSqlite == null) {
            dbSqlite = new DbSqlite();
        }
        return dbSqlite;
    }

    @Override
    public Connection openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                this.connection = DriverManager.getConnection(DATABASE_URL);

                //https://www.sqlite.org/foreignkeys.html#fk_enable
                // to enable foreign key constraint it is necessary to execute after
                connection.prepareStatement("PRAGMA foreign_keys = ON;").executeQuery();
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return this.connection;
    }

    @Override
    public IAccountDao getAccountDao() {
        return new AccountDaoImpl(openConnection());
    }

    @Override
    public IDepartmentDao getDepartmentDao() {
        return new DepartmentDaoImpl(openConnection());
    }

    @Override
    public IHeadOfDepartmentDao getHeadOfDepartmentDao() {
        return new HeadOfDepartmentDaoImpl(openConnection());
    }

    @Override
    public IHodManageDao getHodManageDao() {
        return new HodManageDaoImpl(openConnection());
    }

    @Override
    public IVehicleDao getVehicleDao() {
        return new VehicleDaoImpl(openConnection());
    }

    @Override
    public IVehicleAssignedDao getVehicleAssignedDao() {
        return new VehicleAssignedDaoImpl(openConnection());
    }

    @Override
    public IVehicleTypeDao getVehicleTypeDao() {
        return new VehicleTypeDaoImpl(openConnection());
    }

    @Override
    public ReportFromToDaoImpl getReportFromToDao() {
        return new ReportFromToDaoImpl(openConnection());
    }

    @Override
    public ReportMasterDaoImpl getReportMasterDao() {
        return new ReportMasterDaoImpl(openConnection());
    }

    @Override
    public ReportMaster1DaoImpl getReportMaster1Dao() {
        return new ReportMaster1DaoImpl(openConnection());
    }

    @Override
    public ReportVehiclesDaoImpl getReportVehiclesDao() {
        return new ReportVehiclesDaoImpl(openConnection());
    }

    @Override
    public ReportDepartmentsDaoImpl getReportDepartmentsDao() {
        return new ReportDepartmentsDaoImpl(openConnection());
    }

    @Override
    public IMD5ModelDao getMD5ModelDao() {
        return new MD5ModelDaoImpl(FILE_GENERATED_PARENT_FOLDER_NAME, MD5_MODEL_GENERATED_FOLDER_NAME);
    }

    @Override
    public ILoginModelDao getLoginModelDao() {
        return new LoginModelDaoImpl(FILE_GENERATED_PARENT_FOLDER_NAME, LOGIN_MODEL_GENERATED_FOLDER_NAME);
    }
}
