package com.godavari.appsnest.fms.ui.eventbus.event;

import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.ui.eventbus.Postable;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class DepartmentSelectionEvent implements Postable {

    private Department department;

    public DepartmentSelectionEvent(Department department) {
        this.department = department;
    }
}
