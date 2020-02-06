package com.godavari.appsnest.fms.ui.controller.combobox;

import com.godavari.appsnest.fms.dao.model.VehicleAssigned;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.HodManageSelectionEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.VehicleAssignedRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.VehicleAssignedSelectionEvent;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXComboBox;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Log4j
public class SavedVehicleAssignedComboBoxController extends BaseComboBoxController<VehicleAssigned> {

    @FXML
    private JFXComboBox<VehicleAssigned> jfxComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");
        MyEventBus.register(this);
        jfxComboBox.setConverter(this);
        jfxComboBox.valueProperty().addListener(this);
        jfxComboBox.setEditable(true);
    }

    private void refreshList() {
        log.info("refreshList");
        try {
            //todo check this
            jfxComboBox.getItems().clear();
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public String toString(VehicleAssigned vehicleAssigned) {
        if (vehicleAssigned != null) {
            return vehicleAssigned.getVehicle().getVehicleNo() + Utility.TEXT_CONNECTOR + vehicleAssigned.getVehicle().getVehicleType().getType();
        }
        return null;
    }

    @Override
    public VehicleAssigned fromString(String text) {
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(text.trim())) {
            return null;
        }

        FilteredList<VehicleAssigned> filteredList = jfxComboBox.getItems().filtered(new Predicate<VehicleAssigned>() {
            @Override
            public boolean test(VehicleAssigned vehicleAssigned) {
                String[] textSplit = text.split(Utility.TEXT_CONNECTOR);
                if (textSplit.length > 0 && vehicleAssigned.getVehicle().getVehicleNo().equals(textSplit[0])) {
                    return true;
                }
                return false;
            }
        });
        return filteredList.get(0);
    }

    @Override
    public void changed(ObservableValue<? extends VehicleAssigned> observableValue, VehicleAssigned oldValue, VehicleAssigned newValue) {
        if (newValue != null) {
            log.info("changed: " + newValue);
            MyEventBus.post(new VehicleAssignedSelectionEvent(newValue));
        }
    }

    @Subscribe
    public void selectHodManage(HodManageSelectionEvent hodManageSelectionEvent) {
        try {
            log.info("selectHodManage");
            if (hodManageSelectionEvent != null) {
                refreshList();
                jfxComboBox.getItems().setAll(VehicleAssigned.getRowByDepartmentIdAndCurrent(hodManageSelectionEvent.getHodManage().getDepartment().getId(), true));
                new AutoCompletionTextFieldBinding(jfxComboBox.getEditor(),
                        SuggestionProvider.create(jfxComboBox.getItems()), this);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Subscribe
    protected void refreshList(VehicleAssignedRefreshEvent VehicleAssignedRefreshEvent) {
        log.info("refreshList");
        if (VehicleAssignedRefreshEvent != null) {
            jfxComboBox.getSelectionModel().clearSelection();
            refreshList();
        }
    }

    @Subscribe
    protected void selectVehicleAssigned(VehicleAssignedSelectionEvent vehicleAssignedSelectionEvent) {
        log.info("selectVehicleAssigned");
        if (vehicleAssignedSelectionEvent != null) {
            jfxComboBox.getSelectionModel().select(vehicleAssignedSelectionEvent.getVehicleAssigned());
        }
    }
}
