package com.godavari.appsnest.fms.ui.controller;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.insert.InsertHodActionPerformed;
import com.godavari.appsnest.fms.core.listener.delete.DeleteHodActionPerformed;
import com.godavari.appsnest.fms.core.listener.update.UpdateHodActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.Subscriber;
import com.godavari.appsnest.fms.ui.eventbus.event.HodRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.HodSelectionEvent;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class HodFrameController extends ChildBaseFrameController<HeadOfDepartment>{

    private static final String LOG_TAG = HodFrameController.class.getSimpleName();

    @FXML
    private JFXTextField jfxTfHodName;

    private String name;

    private HeadOfDepartment headOfDepartment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");
        MyEventBus.register(this);

        // remove thi all from all frame
        lSavedListTitle.setText(resourceBundle.getString("saved_hod"));
        lFrameTitle.setText(resourceBundle.getString("head_of_department_string"));

        updateUi(null);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        log.info("button pressed: " + actionEvent.getSource());
        if (actionEvent.getSource() == jfxBReset) {
            updateUi(null);
        } else if (actionEvent.getSource() == jfxBInsert) {
            populateObject();
            new InsertHodActionPerformed(this, headOfDepartment).actionPerform();
        } else if (actionEvent.getSource() == jfxBDelete) {
            new DeleteHodActionPerformed(this, headOfDepartment).actionPerform();
        } else if (actionEvent.getSource() == jfxBUpdate) {
            populateObject();
            new UpdateHodActionPerformed(this, headOfDepartment).actionPerform();
        }
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        super.onActionPerformedResult(resultMessage);
        log.info(resultMessage);
        if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_SUCCESS) {
            switch (resultMessage.getResultCode()) {
                case SUCCESS_CODE_INSERT_HEAD_OF_DEPARTMENT:
                case SUCCESS_CODE_UPDATE_HEAD_OF_DEPARTMENT:
                case SUCCESS_CODE_DELETE_HEAD_OF_DEPARTMENT:
                    MyEventBus.post(new HodRefreshEvent());
                    updateUi(null);
                    Utility.showSnackBar((Pane)jfxBReset.getScene().getRoot(), resultMessage.getResultString());
                    break;
            }
        } else if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL) {
            switch (resultMessage.getResultCode()) {
                case FAIL_CODE_INSERT_HEAD_OF_DEPARTMENT_INPUT_ISSUE:
                case FAIL_CODE_UPDATE_HEAD_OF_DEPARTMENT_INPUT_ISSUE:
                case FAIL_CODE_DELETE_HEAD_OF_DEPARTMENT_ISSUE:
                    Utility.showDialogBox(Alert.AlertType.ERROR, resultMessage.getResultString());
                    break;
            }
        }
    }

    @Subscribe
    protected void selectHod(HodSelectionEvent hodSelectionEvent) {
        log.info("selectHod hod selected: " + hodSelectionEvent.getHeadOfDepartment());
        if (hodSelectionEvent != null) {
            updateUi(hodSelectionEvent.getHeadOfDepartment());
        }
    }

    @Override
    protected void updateUi(HeadOfDepartment headOfDepartment) {
        log.info("updateUi");
        if (headOfDepartment == null) {
            this.headOfDepartment = null;

            jfxBInsert.setDisable(false);
            jfxBDelete.setDisable(true);
            jfxBUpdate.setDisable(true);

            jfxTfHodName.setText(null);

            jfxTfHodName.requestFocus();
        } else {
            this.headOfDepartment = headOfDepartment;

            jfxBInsert.setDisable(true);
            jfxBDelete.setDisable(false);
            jfxBUpdate.setDisable(false);

            jfxTfHodName.setText(headOfDepartment.getName());
        }
    }

    @Override
    protected void populateObject() {
        name = jfxTfHodName.getText();
        if (headOfDepartment == null) {
            headOfDepartment = new HeadOfDepartment();
        }
        headOfDepartment.setName(name);
    }
}

