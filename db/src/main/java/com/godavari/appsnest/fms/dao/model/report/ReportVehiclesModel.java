package com.godavari.appsnest.fms.dao.model.report;

import com.godavari.appsnest.fms.dao.concrete.report.ReportVehiclesDaoImpl;
import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.model.Vehicle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Log4j
public class ReportVehiclesModel extends BaseReportModel {
    private List<Vehicle> vehicleList;
    private List<VehicleAllModel> vehicleAllModelList = new ArrayList<>();

    public List<VehicleAllModel> getRowFromToDate() throws SQLException {
        vehicleList = Vehicle.getAll();
        for (Vehicle vehicle : vehicleList) {
            VehicleAllModel vehicleAllModel = getReportVehiclesDaoImpl().getRowByFromToDate(vehicle, fromLocalDate, toLocalDate);
            vehicleAllModelList.add(vehicleAllModel);
        }

        return vehicleAllModelList;
    }

    private static ReportVehiclesDaoImpl getReportVehiclesDaoImpl() {
        return DaoFactory.getDatabase().getReportVehiclesDao();
    }

    @Override
    public boolean isAnyDataAvailableToGenerateReport() {
        if (vehicleAllModelList != null && !vehicleAllModelList.isEmpty()) {
            for (VehicleAllModel vehicleAllModel : vehicleAllModelList) {
                if (vehicleAllModel.getDepartmentVehicleRowModelList() != null &&
                        !vehicleAllModel.getDepartmentVehicleRowModelList().isEmpty()) {
                    return true;

                }
            }
        }
        return false;
    }
}
