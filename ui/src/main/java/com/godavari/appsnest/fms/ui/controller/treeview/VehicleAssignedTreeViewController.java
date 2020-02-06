package com.godavari.appsnest.fms.ui.controller.treeview;

import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.model.Vehicle;
import com.godavari.appsnest.fms.dao.model.VehicleAssigned;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.VehicleAssignedRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.VehicleAssignedSelectionEvent;
import com.godavari.appsnest.fms.ui.utility.ResourceString;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXTreeView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Log4j
public class VehicleAssignedTreeViewController extends BaseTreeViewController {

    @FXML
    private JFXTreeView jfxTreeView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");

        MyEventBus.register(this);
        jfxTreeView.setShowRoot(false);
        jfxTreeView.getSelectionModel().selectedItemProperty().addListener(this);
        jfxTreeView.setCellFactory(this);
        refreshSavedList();
    }

    @Override
    public Object call(Object treeView) {
        return new TreeCell() {
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (item instanceof VehicleAssigned) {
                        setText(((VehicleAssigned) item).getVehicle().getVehicleNo());
                    } else if (item instanceof String)
                        setText((String) item);
                }
            }
        };
    }

    private void refreshSavedList() {
        try {

            TreeItem<String> rootTreeItem = new TreeItem<>(ResourceString.getString("vehicle_assigned"));
            jfxTreeView.setRoot(rootTreeItem);

            List<VehicleAssigned> vehicleAssignedList = VehicleAssigned.getAllByCurrent(true);

            Map<Department, Vehicle> departmentVehicleMap = new HashMap<>();
            for (VehicleAssigned vehicleAssigned : vehicleAssignedList) {
                departmentVehicleMap.put(vehicleAssigned.getDepartment(), vehicleAssigned.getVehicle());
            }

            for (VehicleAssigned vehicleAssigned : vehicleAssignedList) {
                Department department = vehicleAssigned.getDepartment();

                TreeItem vehicleLeaf = new TreeItem(vehicleAssigned);
                boolean found = false;

                List<TreeItem<String>> departmentTreeItemList = jfxTreeView.getRoot().getChildren();
                for (TreeItem<String> treeItem : departmentTreeItemList) {
                    if (treeItem.getValue().equals(department.getName())) {
                        treeItem.getChildren().add(vehicleLeaf);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    TreeItem<String> deptNode = new TreeItem<String>(department.getName());
                    jfxTreeView.getRoot().getChildren().add(deptNode);
                    deptNode.setExpanded(true);
                    deptNode.getChildren().add(vehicleLeaf);
                }
            }

        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
        log.info("changed: " + newValue);
        if (newValue != null && (((TreeItem) newValue).getValue() instanceof VehicleAssigned)) {
            log.info("VehicleAssigned selected: " + newValue);
            VehicleAssigned vehicleAssigned = (VehicleAssigned) ((TreeItem) newValue).getValue();
            MyEventBus.post(new VehicleAssignedSelectionEvent(vehicleAssigned));
        }
    }

    @Subscribe
    public void refreshTreeView(VehicleAssignedRefreshEvent vehicleAssignedRefreshEvent) {
        log.info("refreshTreeView");
        if (vehicleAssignedRefreshEvent != null) {
            jfxTreeView.getSelectionModel().clearSelection();
            jfxTreeView.setRoot(null);
            refreshSavedList();
        }
    }
}
