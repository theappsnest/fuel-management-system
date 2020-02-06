package com.godavari.appsnest.fms.ui.controller;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.delete.DeleteVehicleTypeActionPerformed;
import com.godavari.appsnest.fms.core.listener.insert.InsertVehicleTypeActionPerformed;
import com.godavari.appsnest.fms.core.listener.update.UpdateVehicleTypeActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.dao.model.VehicleType;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.Subscriber;
import com.godavari.appsnest.fms.ui.eventbus.event.VehicleTypeRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.VehicleTypeSelectionEvent;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class VehicleTypeFrameController extends ChildBaseFrameController<VehicleType> {

    private static final String LOG_TAG = VehicleTypeFrameController.class.getSimpleName();

    @FXML
    private JFXTextField jfxTfVehicleType;

    private String type;

    private VehicleType vehicleType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");

        MyEventBus.register(this);

        lSavedListTitle.setText(resourceBundle.getString("saved_vehicle_type"));
        lFrameTitle.setText(resourceBundle.getString("vehicle_type_string"));

        updateUi(null);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        log.info("button pressed: " + actionEvent.getSource());
        if (actionEvent.getSource() == jfxBReset) {
            updateUi(null);
        } else if (actionEvent.getSource() == jfxBInsert) {
            populateObject();
            new InsertVehicleTypeActionPerformed(this, vehicleType).actionPerform();
        } else if (actionEvent.getSource() == jfxBDelete) {
            new DeleteVehicleTypeActionPerformed(this, vehicleType).actionPerform();
        } else if (actionEvent.getSource() == jfxBUpdate) {
            populateObject();
            new UpdateVehicleTypeActionPerformed(this, vehicleType).actionPerform();
        }
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        super.onActionPerformedResult(resultMessage);
        log.info(resultMessage);
        String resultString = resultMessage.getResultString();
        if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_SUCCESS) {
            switch (resultMessage.getResultCode()) {
                case SUCCESS_CODE_INSERT_VEHICLE_TYPE:
                case SUCCESS_CODE_DELETE_VEHICLE_TYPE:
                case SUCCESS_CODE_UPDATE_VEHICLE_TYPE:
                    MyEventBus.post(new VehicleTypeRefreshEvent());
                    updateUi(null);
                    Utility.showDialogBox(Alert.AlertType.INFORMATION, resultString);
                    break;
            }
        } else if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL) {
            switch (resultMessage.getResultCode()) {
                case FAIL_CODE_INSERT_VEHICLE_TYPE_INPUT_ISSUE:
                case FAIL_CODE_DELETE_VEHICLE_TYPE_ISSUE:
                case FAIL_CODE_UPDATE_VEHICLE_TYPE_INPUT_ISSUE:
                    Utility.showDialogBox(Alert.AlertType.ERROR, resultString);
                    break;
            }
        }
    }

    @Subscribe
    protected void selectVehicleType(VehicleTypeSelectionEvent vehicleTypeSelectionEvent) {
        log.info("selectVehicleType: " + vehicleTypeSelectionEvent.getVehicleType());
        if (vehicleTypeSelectionEvent != null) {
            updateUi(vehicleTypeSelectionEvent.getVehicleType());
        }
    }

    @Override
    protected void updateUi(VehicleType vehicleType) {
        log.info("updateUi");
        if (vehicleType == null) {
            this.vehicleType = null;

            jfxBInsert.setDisable(false);
            jfxBDelete.setDisable(true);
            jfxBUpdate.setDisable(true);

            jfxTfVehicleType.setText(null);

            jfxTfVehicleType.requestFocus();
        } else {
            this.vehicleType = vehicleType;

            jfxBInsert.setDisable(true);
            jfxBDelete.setDisable(false);
            jfxBUpdate.setDisable(false);

            jfxTfVehicleType.setText(vehicleType.getType());
        }
    }

    @Override
    protected void populateObject() {
        type = jfxTfVehicleType.getText();
        if (vehicleType == null) {
            vehicleType = new VehicleType(type);
        }
        vehicleType.setType(type);
    }
}

