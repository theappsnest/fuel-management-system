package com.godavari.appsnest.fms.dao.model.report;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class BaseReportModel {
    protected LocalDate fromLocalDate;
    protected LocalDate toLocalDate;

    public abstract boolean isAnyDataAvailableToGenerateReport();
}
