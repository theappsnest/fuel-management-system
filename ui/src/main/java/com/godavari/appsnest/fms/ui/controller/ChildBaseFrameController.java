package com.godavari.appsnest.fms.ui.controller;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.core.utility.PropertyKey;
import com.godavari.appsnest.fms.dao.model.LoginModel;
import com.godavari.appsnest.fms.dao.utility.PropertiesUtil;
import com.godavari.appsnest.fms.ui.eventbus.Subscriber;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.FAIL_CODE_EXCEPTION_THROWN;
import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.FAIL_CODE_SQL_EXCEPTION_THROWN;

@Log4j
public abstract class ChildBaseFrameController<T> extends BaseFrameController<T> implements Initializable, EventHandler<ActionEvent>, IOnActionPerformed, Subscriber {

    private static final String LOG_TAG = ChildBaseFrameController.class.getSimpleName();

    @FXML
    protected Label lSavedListTitle;

    @FXML
    protected Label lFrameTitle;

    @FXML
    protected JFXButton jfxBReset;

    @FXML
    protected JFXButton jfxBInsert;

    @FXML
    protected JFXButton jfxBDelete;

    @FXML
    protected JFXButton jfxBUpdate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        jfxBReset.setOnAction(this);
        jfxBInsert.setOnAction(this);
        jfxBDelete.setOnAction(this);
        jfxBUpdate.setOnAction(this);

        String currentUserLogin = PropertiesUtil.getInstance().getPropertyValue(PropertyKey.CURRENT_USER_LOGIN);
        if (LoginModel.USER_TYPE.equals(currentUserLogin)) {
            jfxBDelete.visibleProperty().setValue(false);
            jfxBUpdate.visibleProperty().setValue(false);
        }
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        log.info("onActionPerformedResult, resultMessage: " + resultMessage);
        if (ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL == resultMessage.getResultType()) {
            switch (resultMessage.getResultCode()) {
                case FAIL_CODE_SQL_EXCEPTION_THROWN:
                case FAIL_CODE_EXCEPTION_THROWN:
                    Utility.showDialogBox(Alert.AlertType.ERROR, resultMessage.getResultString());
                    break;
            }
        }
    }
}
