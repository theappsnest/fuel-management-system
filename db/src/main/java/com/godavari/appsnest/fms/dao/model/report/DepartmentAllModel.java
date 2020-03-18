package com.godavari.appsnest.fms.dao.model.report;

import com.godavari.appsnest.fms.dao.model.Department;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Getter
@Setter
@Log4j
public class DepartmentAllModel {
    private Department department;
    private List<DepartmentVehicleRowModel> departmentVehicleRowModelList;

    public void formatObject() {
        if (department != null) {
            department.formatObject();
        }
    }
}
