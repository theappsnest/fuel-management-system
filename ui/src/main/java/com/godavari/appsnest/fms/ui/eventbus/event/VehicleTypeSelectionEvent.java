package com.godavari.appsnest.fms.ui.eventbus.event;

import com.godavari.appsnest.fms.dao.model.VehicleType;
import com.godavari.appsnest.fms.ui.eventbus.Postable;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class VehicleTypeSelectionEvent implements Postable {

    private VehicleType vehicleType;

    public VehicleTypeSelectionEvent(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
