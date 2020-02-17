package com.godavari.appsnest.fms.ui.controller;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.listener.delete.DeleteHodManageActionPerformed;
import com.godavari.appsnest.fms.core.listener.insert.InsertHodManageActionPerformed;
import com.godavari.appsnest.fms.core.listener.update.UpdateHodManageActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;
import com.godavari.appsnest.fms.dao.model.HodManage;
import com.godavari.appsnest.fms.ui.eventbus.MyEventBus;
import com.godavari.appsnest.fms.ui.eventbus.event.HodManageRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.HodManageSelectionEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.HodRefreshEvent;
import com.godavari.appsnest.fms.ui.eventbus.event.HodSelectionEvent;
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
public class HodManageFrameController extends ChildBaseFrameController<HodManage>{

    private static final String LOG_TAG = HodManageFrameController.class.getSimpleName();

    @FXML
    private JFXTextField jfxTfDepartmentName;

    private Department department;
    private HeadOfDepartment headOfDepartment;

    private HodManage hodManage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        log.info("initialize");

        MyEventBus.register(this);

        lSavedListTitle.setText(resourceBundle.getString("saved_hod_manage"));
        lFrameTitle.setText(resourceBundle.getString("hod_manage_string"));

        updateUi(null);
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        super.onActionPerformedResult(resultMessage);
        log.info(resultMessage);
        String resultString = resultMessage.getResultString();
        if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_SUCCESS) {
            switch (resultMessage.getResultCode()) {
                case SUCCESS_CODE_INSERT_HOD_MANAGE:
                case SUCCESS_CODE_UPDATE_HOD_MANAGE:
                case SUCCESS_CODE_DELETE_HOD_MANAGE:
                    MyEventBus.post(new HodManageRefreshEvent());
                    updateUi(null);
                    Utility.showSnackBar((Pane)jfxBReset.getScene().getRoot(), resultMessage.getResultString());
                    break;
            }
        } else if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL) {
            switch (resultMessage.getResultCode()) {
                case FAIL_CODE_INSERT_HOD_MANAGE_INPUT_ISSUE:
                case FAIL_CODE_UPDATE_HOD_MANAGE_INPUT_ISSUE:
                case FAIL_CODE_DELETE_HOD_MANAGE_ISSUE:
                    Utility.showDialogBox(Alert.AlertType.ERROR, resultString);
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
            new InsertHodManageActionPerformed(this, hodManage).actionPerform();
        } else if (actionEvent.getSource() == jfxBDelete) {
            new DeleteHodManageActionPerformed(this, hodManage).actionPerform();
        } else if (actionEvent.getSource() == jfxBUpdate) {
            populateObject();
            new UpdateHodManageActionPerformed(this, hodManage).actionPerform();
        }
    }

    @Override
    protected void updateUi(HodManage hodManage) {
        log.info("updateUi");
        if (hodManage == null) {
            this.hodManage = null;
            department = null;
            headOfDepartment = null;

            jfxBInsert.setDisable(false);
            jfxBDelete.setDisable(true);
            jfxBUpdate.setDisable(true);

            jfxTfDepartmentName.setText(null);
            MyEventBus.post(new HodRefreshEvent());

            jfxTfDepartmentName.requestFocus();
        } else {
            this.hodManage = hodManage;
            department = hodManage.getDepartment();
            headOfDepartment = hodManage.getHeadOfDepartment();

            jfxBInsert.setDisable(true);
            jfxBDelete.setDisable(false);
            jfxBUpdate.setDisable(false);

            jfxTfDepartmentName.setText(department.getName());
            MyEventBus.post(new HodSelectionEvent(headOfDepartment));
        }
    }

    @Override
    protected void populateObject() {
        log.info("populateObject");
        if (hodManage == null) {
            hodManage = new HodManage();
        }
        if (department == null)
        {
            department = new Department();
        }
        department.setName(jfxTfDepartmentName.getText());
        hodManage.setDepartment(department);
        hodManage.setHeadOfDepartment(headOfDepartment);
    }

    @Subscribe
    protected void selectHodManage(HodManageSelectionEvent hodManageSelectionEvent) {
        log.info("selectHodManage");
        if (hodManageSelectionEvent != null) {
            updateUi(hodManageSelectionEvent.getHodManage());
        }
    }

    @Subscribe
    protected void selectHod(HodSelectionEvent hodSelectionEvent) {
        log.info("selectHod hod selected: " + hodSelectionEvent.getHeadOfDepartment());
        if (hodSelectionEvent != null) {
            headOfDepartment = hodSelectionEvent.getHeadOfDepartment();
        }
    }
}
