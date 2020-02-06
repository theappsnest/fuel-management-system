package com.godavari.appsnest.fms.ui.controller.combobox;

import com.godavari.appsnest.fms.dao.model.VehicleType;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.VehicleTypeRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.VehicleTypeSelectionEvent;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class SavedVehicleTypeComboBoxController extends BaseComboBoxController<VehicleType> {

    private static final String LOG_TAG = SavedVehicleTypeComboBoxController.class.getSimpleName();

    @FXML
    private JFXComboBox<VehicleType> jfxComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");
        MyEventBus.register(this);
        jfxComboBox.setConverter(this);
        jfxComboBox.valueProperty().addListener(this);
        refreshList();
    }

    private void refreshList() {
        log.info("refreshList");
        try {
            jfxComboBox.getItems().setAll(VehicleType.getAll());
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public String toString(VehicleType vehicleType) {
        if (vehicleType != null) {
            return vehicleType.getType();
        }
        return null;
    }

    @Override
    public VehicleType fromString(String s) {
        return null;
    }

    @Override
    public void changed(ObservableValue<? extends VehicleType> observableValue, VehicleType oldValue, VehicleType newValue) {
        if (newValue != null) {
            log.info("changed: " + newValue);
            MyEventBus.post(new VehicleTypeSelectionEvent(newValue));
        }
    }

    @Subscribe
    protected void refreshList(VehicleTypeRefreshEvent vehicleTypeRefreshEvent) {
        log.info("refreshList");
        if (vehicleTypeRefreshEvent != null) {
            jfxComboBox.getSelectionModel().clearSelection();
            refreshList();
        }
    }

    @Subscribe
    protected void selectVehicleType(VehicleTypeSelectionEvent vehicleTypeSelectionEvent) {
        log.info("selectVehicleType");
        if (vehicleTypeSelectionEvent != null) {
            jfxComboBox.getSelectionModel().select(vehicleTypeSelectionEvent.getVehicleType());
        }
    }
}
