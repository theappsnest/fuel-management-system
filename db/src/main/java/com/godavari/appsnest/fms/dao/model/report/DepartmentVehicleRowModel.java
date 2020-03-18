package com.godavari.appsnest.fms.dao.model.report;

import com.godavari.appsnest.fms.dao.model.Account;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Getter
@Setter
@Log4j
public class DepartmentVehicleRowModel {
    private Account account;
    private double total;

    public void formatObject() {
        if (account != null) {
            account.formatObject();
        }
    }
}
