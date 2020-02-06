package com.godavari.appsnest.fms.dao.model.report;

import com.godavari.appsnest.fms.dao.concrete.report.ReportDepartmentsDaoImpl;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.model.Department;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Log4j
public class ReportDepartmentsModel extends BaseReportModel {

    private List<Department> departmentList;
    private List<DepartmentAllModel> departmentAllModels = new ArrayList<>();

    public List<DepartmentAllModel> getRowFromToDate() throws SQLException {
        departmentList = Department.getAll();
        for (Department department : departmentList) {
            DepartmentAllModel departmentAllModel = getReportDepartmentsDaoImpl().getRowByFromToDate(department, fromLocalDate, toLocalDate);
            departmentAllModels.add(departmentAllModel);
        }
        return departmentAllModels;
    }

    private static ReportDepartmentsDaoImpl getReportDepartmentsDaoImpl() {
        return DaoFactory.getDatabase().getReportDepartmentsDao();
    }

    @Override
    public boolean isAnyDataAvailableToGenerateReport() {
        if (departmentAllModels != null && !departmentAllModels.isEmpty()) {
            for (DepartmentAllModel departmentAllModel : departmentAllModels) {
                if (departmentAllModel.getDepartmentVehicleRowModelList() != null &&
                        !departmentAllModel.getDepartmentVehicleRowModelList().isEmpty()) {
                    return true;

                }
            }
        }
        return false;
    }
}
