package com.godavari.appsnest.fms.ui.controller.combobox;

import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.DepartmentRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.DepartmentSelectionEvent;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class SavedDepartmentComboBoxController extends BaseComboBoxController<Department> {

    private static final String LOG_TAG = SavedDepartmentComboBoxController.class.getSimpleName();

    @FXML
    private JFXComboBox<Department> jfxComboBox;

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
            jfxComboBox.getItems().setAll(Department.getAll());
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public String toString(Department headOfDepartment) {
        if (headOfDepartment != null) {
            return headOfDepartment.getName();
        }
        return null;
    }

    @Override
    public Department fromString(String s) {
        return null;
    }

    @Override
    public void changed(ObservableValue<? extends Department> observableValue, Department oldValue, Department newValue) {
        if (newValue != null) {
            log.info("changed: " + newValue);
            MyEventBus.post(new DepartmentSelectionEvent(newValue));
        }
    }

    @Subscribe
    protected void refreshList(DepartmentRefreshEvent departmentRefreshEvent) {
        log.info("refreshList");
        if (departmentRefreshEvent != null) {
            jfxComboBox.getSelectionModel().clearSelection();
            refreshList();
        }
    }

    @Subscribe
    protected void selectDepartment(DepartmentSelectionEvent departmentSelectionEvent) {
        log.info("selectHod");
        if (departmentSelectionEvent != null) {
            jfxComboBox.getSelectionModel().select(departmentSelectionEvent.getDepartment());
        }
    }
}
