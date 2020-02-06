package com.godavari.appsnest.fms.ui.controller.list;

import com.godavari.appsnest.fms.dao.model.VehicleType;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.VehicleTypeRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.VehicleTypeSelectionEvent;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class SavedVehicleTypeListController extends BaseListController<VehicleType>{

    private static final String LOG_TAG = SavedVehicleTypeListController.class.getSimpleName();

    @FXML
    private JFXListView<VehicleType> jfxListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");
        MyEventBus.register(this);
        jfxListView.setCellFactory(this);
        jfxListView.getSelectionModel().selectedItemProperty().addListener(this);
        refreshSavedList();
    }

    private void refreshSavedList() {
        try {
            jfxListView.getItems().setAll(VehicleType.getAll());
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public ListCell<VehicleType> call(ListView<VehicleType> vehicleTypeListView) {
        return new ListCell<VehicleType>() {
            @Override
            protected void updateItem(VehicleType vehicleType, boolean empty) {
                super.updateItem(vehicleType, empty);
                if (empty || vehicleType == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(vehicleType.getType());
                }
            }
        };
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
        if (vehicleTypeRefreshEvent != null) {
            refreshSavedList();
        }
    }
}
