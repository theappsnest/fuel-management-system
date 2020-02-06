package com.godavari.appsnest.fms.ui.eventbus.event;

import com.godavari.appsnest.fms.dao.model.VehicleAssigned;
import com.godavari.appsnest.fms.ui.eventbus.Postable;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class VehicleAssignedSelectionEvent implements Postable {

    private VehicleAssigned vehicleAssigned;

    public VehicleAssignedSelectionEvent(VehicleAssigned vehicleAssigned) {
        this.vehicleAssigned = vehicleAssigned;
    }
}
