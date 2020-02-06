package com.godavari.appsnest.fms.ui.controller.startactivation;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.listener.startactivation.CheckActivationActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.MD5Model;
import com.godavari.appsnest.fms.ui.controller.BaseFrameController;
import com.godavari.appsnest.fms.ui.frame.MainFrame;
import com.godavari.appsnest.fms.ui.frame.startactivation.LoginScreenFrame;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.net.URL;
import java.util.ResourceBundle;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;
import static com.godavari.appsnest.fms.dao.model.MD5Model.SALT_LENGTH;

@Log4j
public class ActivationScreenController<T> extends BaseFrameController<T> {

    @FXML
    private AnchorPane apMain;
    @FXML
    private TextField tfPasswordToHash;
    @FXML
    private TextField tfSalt1;
    @FXML
    private TextField tfSalt2;
    @FXML
    private TextField tfSalt3;
    @FXML
    private TextField tfSalt4;
    @FXML
    private TextField tfSalt5;

    @FXML
    private JFXButton bJfxActivate;

    @FXML
    private JFXButton bJfxClose;

    private MD5Model userMD5Model;
    private String passwordToHash;
    private byte[] salt = new byte[SALT_LENGTH];

    private Stage secondaryStage = new Stage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("initialize");

        bJfxActivate.setOnAction(this);
        bJfxClose.setOnAction(this);

        for (int i = 0; i < salt.length; i++) {
            salt[i] = -1;
        }
    }

    @Override
    protected void updateUi(Object object) {

    }

    @Override
    protected void populateObject() {
        log.info("populateObjectVariables");
        try {
            passwordToHash = tfPasswordToHash.getText();
            resetCheckPasswordTextField();
            if (!StringUtils.isEmpty(tfSalt1.getText())) {
                salt[0] = new Byte(tfSalt1.getText());
            }
            if (!StringUtils.isEmpty(tfSalt2.getText())) {
                salt[1] = new Byte(tfSalt2.getText());
            }
            if (!StringUtils.isEmpty(tfSalt3.getText())) {
                salt[2] = new Byte(tfSalt3.getText());
            }
            if (!StringUtils.isEmpty(tfSalt4.getText())) {
                salt[3] = new Byte(tfSalt4.getText());
            }
            if (!StringUtils.isEmpty(tfSalt5.getText())) {
                salt[4] = new Byte(tfSalt5.getText());
            }
        } catch (Exception e) {
            log.error("populateObjectVariables", e);
        }
        userMD5Model = new MD5Model(passwordToHash, salt);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        log.info("handle button presses: " + actionEvent.getSource());
        if (actionEvent.getSource() == bJfxActivate) {
            populateObject();
            new CheckActivationActionPerformed(this, userMD5Model).actionPerform();
        } else if (actionEvent.getSource() == bJfxClose) {
            ((Stage) apMain.getScene().getWindow()).close();
        }
    }

    private void resetCheckPasswordTextField() {
        for (int i = 0; i < salt.length; i++) {
            salt[i] = -1;
        }
        if (!StringUtils.isEmpty(tfSalt1.getText())) {
            salt[0] = 0;
        }
        if (!StringUtils.isEmpty(tfSalt2.getText())) {
            salt[1] = 0;
        }
        if (!StringUtils.isEmpty(tfSalt3.getText())) {
            salt[2] = 0;
        }
        if (!StringUtils.isEmpty(tfSalt4.getText())) {
            salt[3] = 0;
        }
        if (!StringUtils.isEmpty(tfSalt5.getText())) {
            salt[4] = 0;
        }
    }

    private void startNextStage() {
        log.info("startNextStage");
        try {
            secondaryStage = new Stage();
            secondaryStage.setScene(new Scene(Utility.getParent(LoginScreenFrame.URL)));
            secondaryStage.initStyle(StageStyle.UNDECORATED);

            //close the start screen
            Stage stage = (Stage) apMain.getScene().getWindow();
            stage.close();

            secondaryStage.show();
        } catch (Exception e) {
            log.error("startNextStage loading issue in MainFrame", e);
        }
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        String resultString = resultMessage.getResultString();
        if (ActionPerformedSuccessFailCode.RESULT_TYPE_SUCCESS == resultMessage.getResultType()) {
            switch (resultMessage.getResultCode()) {
                case SUCCESS_CODE_ACTIVATION_ADMIN_SECURE_PASSWORD_MATCHED:
                    Utility.showDialogBox(Alert.AlertType.INFORMATION, resultString);
                    startNextStage();
                    break;
                case SUCCESS_CODE_ACTIVATION_USER_SECURE_PASSWORD_MATCHED:
                    Utility.showDialogBox(Alert.AlertType.INFORMATION, resultString);
                    startNextStage();
                    break;
            }
        } else if (ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL == resultMessage.getResultType()) {
            switch (resultMessage.getResultCode()) {
                case FAIL_CODE_ACTIVATION_USER_INPUT_ISSUE:
                    Utility.showDialogBox(Alert.AlertType.ERROR, resultString);
                    break;
            }
        }
    }
}