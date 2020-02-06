package com.godavari.appsnest.fms.dao.model.report;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Log4j
@Getter
@Setter
public class Master1Model {
    private String departmentName;
    private String vehicleNo;
    private List<Double> monthlyConsumption;
}
