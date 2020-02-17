package com.godavari.appsnest.fms.ui.controller;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.listener.delete.DeleteVehicleAssignedActionPerformed;
import com.godavari.appsnest.fms.core.listener.insert.InsertVehicleAssignedActionPerformed;
import com.godavari.appsnest.fms.core.listener.update.UpdateVehicleAssignedActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.model.Vehicle;
import com.godavari.appsnest.fms.dao.model.VehicleAssigned;
import com.godavari.appsnest.fms.dao.model.VehicleType;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.*;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class VehicleAssignedFrameController extends ChildBaseFrameController<VehicleAssigned>{

    private static final String LOG_TAG = VehicleAssignedFrameController.class.getSimpleName();

    @FXML
    private JFXTextField jfxTfVehicleNo;

    private Department department;
    private Vehicle vehicle;
    private VehicleType vehicleType;

    private boolean current;

    private VehicleAssigned vehicleAssigned;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");

        MyEventBus.register(this);

        lSavedListTitle.setText(resourceBundle.getString("saved_vehicle_assigned"));
        lFrameTitle.setText(resourceBundle.getString("vehicle_assigned_string"));

        updateUi(null);
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        super.onActionPerformedResult(resultMessage);
        log.info(resultMessage);
        if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_SUCCESS) {
            switch (resultMessage.getResultCode()) {
                case SUCCESS_CODE_INSERT_VEHICLE_ASSIGNED:
                case SUCCESS_CODE_DELETE_VEHICLE_ASSIGNED:
                case SUCCESS_CODE_UPDATE_VEHICLE_ASSIGNED:
                    MyEventBus.post(new VehicleAssignedRefreshEvent());
                    updateUi(null);
                    Utility.showSnackBar((Pane)jfxBReset.getScene().getRoot(), resultMessage.getResultString());
                    break;
            }
        } else if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL) {
            switch (resultMessage.getResultCode()) {
                case FAIL_CODE_INSERT_VEHICLE_ASSIGNED_INPUT_ISSUE:
                case FAIL_CODE_DELETE_VEHICLE_ASSIGNED_ISSUE:
                case FAIL_CODE_UPDATE_VEHICLE_ASSIGNED_INPUT_ISSUE:
                    Utility.showDialogBox(Alert.AlertType.ERROR, resultMessage.getResultString());
                    break;
            }
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        log.info("button pressed: " + actionEvent.getSource());
        if (actionEvent.getSource() == jfxBReset) {
            updateUi(null);
        } else if (actionEvent.getSource() == jfxBInsert) {
            populateObject();
            new InsertVehicleAssignedActionPerformed(this, vehicleAssigned).actionPerform();
        } else if (actionEvent.getSource() == jfxBDelete) {
            new DeleteVehicleAssignedActionPerformed(this, vehicleAssigned).actionPerform();
        } else if (actionEvent.getSource() == jfxBUpdate) {
            populateObject();
            new UpdateVehicleAssignedActionPerformed(this, vehicleAssigned).actionPerform();
        }
    }

    @Override
    protected void updateUi(VehicleAssigned vehicleAssigned) {
        log.info("updateUi");
        if (vehicleAssigned == null) {
            this.vehicleAssigned = null;
            department = null;
            vehicle = null;
            vehicleType = null;

            jfxBInsert.setDisable(false);
            jfxBDelete.setDisable(true);
            jfxBUpdate.setDisable(true);

            MyEventBus.post(new DepartmentRefreshEvent());
            jfxTfVehicleNo.setText(null);
            MyEventBus.post(new VehicleTypeRefreshEvent());

            //todo request department
            //jfxTfDepartmentName.requestFocus();
        } else {
            this.vehicleAssigned = vehicleAssigned;
            department = vehicleAssigned.getDepartment();
            vehicle = vehicleAssigned.getVehicle();
            vehicleType = vehicle.getVehicleType();

            jfxBInsert.setDisable(true);
            jfxBDelete.setDisable(false);
            jfxBUpdate.setDisable(false);

            MyEventBus.post(new DepartmentSelectionEvent(department));
            jfxTfVehicleNo.setText(vehicle.getVehicleNo());
            MyEventBus.post(new VehicleTypeSelectionEvent(vehicleType));
        }
    }

    @Override
    protected void populateObject() {
        log.info("populateObject");
        if (vehicle == null) {
            vehicle = new Vehicle();
        }
        if (vehicleAssigned == null) {
            vehicleAssigned = new VehicleAssigned();
        }

        vehicle.setVehicleNo(jfxTfVehicleNo.getText());
        vehicle.setVehicleType(vehicleType);

        vehicleAssigned.setDepartment(department);
        vehicleAssigned.setVehicle(vehicle);
    }


    @Subscribe
    protected void selectVehicleAssigned(VehicleAssignedSelectionEvent vehicleAssignedSelectionEvent) {
        log.info("selectVehicleAssigned");
        if (vehicleAssignedSelectionEvent != null) {
            updateUi(vehicleAssignedSelectionEvent.getVehicleAssigned());
        }
    }

    @Subscribe
    protected void selectDepartment(DepartmentSelectionEvent departmentSelectionEvent) {
        log.info("selectDepartment: " + departmentSelectionEvent.getDepartment());
        if (departmentSelectionEvent != null) {
            department = departmentSelectionEvent.getDepartment();
        }
    }

    @Subscribe
    protected void selectVehicleType(VehicleTypeSelectionEvent vehicleTypeSelectionEvent) {
        log.info("selectVehicleType: " + vehicleTypeSelectionEvent.getVehicleType());
        if (vehicleTypeSelectionEvent != null) {
            vehicleType = vehicleTypeSelectionEvent.getVehicleType();
        }
    }
}
