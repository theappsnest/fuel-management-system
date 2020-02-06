package com.godavari.appsnest.fms.ui.controller.combobox;

import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.HodRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.HodSelectionEvent;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class SavedHodComboBoxController extends BaseComboBoxController<HeadOfDepartment> {

    private static final String LOG_TAG = SavedHodComboBoxController.class.getSimpleName();

    @FXML
    private JFXComboBox<HeadOfDepartment> jfxComboBox;

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
            jfxComboBox.getItems().setAll(HeadOfDepartment.getAll());
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public String toString(HeadOfDepartment headOfDepartment) {
        if (headOfDepartment != null) {
            return headOfDepartment.getName();
        }
        return null;
    }

    @Override
    public HeadOfDepartment fromString(String s) {
        return null;
    }

    @Override
    public void changed(ObservableValue<? extends HeadOfDepartment> observableValue, HeadOfDepartment oldValue, HeadOfDepartment newValue) {
        if (newValue != null) {
            log.info("changed: " + newValue);
            MyEventBus.post(new HodSelectionEvent(newValue));
        }
    }

    @Subscribe
    protected void refreshList(HodRefreshEvent hodRefreshEvent) {
        log.info("refreshList");
        if (hodRefreshEvent != null) {
            jfxComboBox.getSelectionModel().clearSelection();
            refreshList();
        }
    }

    @Subscribe
    protected void selectHod(HodSelectionEvent hodSelectionEvent) {
        log.info("hodSelectionEvent");
        if (hodSelectionEvent != null) {
            jfxComboBox.getSelectionModel().select(hodSelectionEvent.getHeadOfDepartment());
        }
    }
}
