package com.godavari.appsnest.fms.dao.model.report;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.time.LocalDate;

@Log4j
@Getter
@Setter
public class MasterModel {

    private int srNo;
    private LocalDate monthYear;
    private double openingStock;
    private double input;
    private double output;
    private double closingStock;

    public void formatObject()
    {
    }
}
