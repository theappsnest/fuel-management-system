package com.godavari.appsnest.fms.dao.model.report;

import com.godavari.appsnest.fms.dao.model.Vehicle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Getter
@Setter
@Log4j
public class VehicleAllModel {
    private Vehicle vehicle;
    private List<DepartmentVehicleRowModel> departmentVehicleRowModelList;
}
