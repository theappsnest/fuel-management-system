package com.godavari.appsnest.fms.ui.eventbus.event;

import com.godavari.appsnest.fms.dao.model.HodManage;
import com.godavari.appsnest.fms.ui.eventbus.Postable;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class HodManageSelectionEvent implements Postable {

    private HodManage hodManage;

    public HodManageSelectionEvent(HodManage hodManage) {
        this.hodManage = hodManage;
    }
}
