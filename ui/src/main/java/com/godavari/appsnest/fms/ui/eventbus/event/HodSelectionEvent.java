package com.godavari.appsnest.fms.ui.eventbus.event;

import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;
import com.godavari.appsnest.fms.ui.eventbus.Postable;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class HodSelectionEvent implements Postable {

    private HeadOfDepartment headOfDepartment;

    public HodSelectionEvent(HeadOfDepartment headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }
}
