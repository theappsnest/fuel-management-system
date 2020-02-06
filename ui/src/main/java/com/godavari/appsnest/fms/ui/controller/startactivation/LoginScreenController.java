package com.godavari.appsnest.fms.ui.controller.startactivation;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.listener.startactivation.LoginActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.core.utility.PropertyKey;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.LoginModel;
import com.godavari.appsnest.fms.dao.utility.PropertiesUtil;
import com.godavari.appsnest.fms.ui.controller.BaseFrameController;
import com.godavari.appsnest.fms.ui.frame.MainFrame;
import com.godavari.appsnest.fms.ui.utility.Utility;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class LoginScreenController<T> extends BaseFrameController<T> {

    @FXML
    private AnchorPane apMain;

    @FXML
    private JFXTextField tfJfxUserName;

    @FXML
    private JFXPasswordField tfJfxPassword;

    @FXML
    private JFXButton bJfxLogin;

    @FXML
    private JFXButton bJfxClose;

    private String userName;
    private String password;
    private LoginModel loginModel;

    private Stage secondaryStage = new Stage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        bJfxLogin.setOnAction(this);
        bJfxClose.setOnAction(this);
    }

    @Override
    protected void updateUi(T object) {

    }

    @Override
    protected void populateObject() {
        userName = tfJfxUserName.getText();
        password = tfJfxPassword.getText();

        loginModel = new LoginModel(userName, password);
    }

    private void startNextStage() {
        log.info("startNextStage");
        try {
            secondaryStage = new Stage();
            secondaryStage.setScene(new Scene(Utility.getParent(MainFrame.URL)));
            secondaryStage.getIcons().add(new Image("image/company_logo.png"));
            secondaryStage.setTitle(ResourceString.getString("application_name"));

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
        log.info(resultMessage);
        String resultString = resultMessage.getResultString();
        if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_SUCCESS) {
            switch (resultMessage.getResultCode()) {
                case SUCCESS_CODE_LOGIN_DEVELOPER_USERNAME_PASSWORD_MATCHED:
                case SUCCESS_CODE_LOGIN_ADMIN_TYPE_USERNAME_PASSWORD_MATCHED:
                    PropertiesUtil.getInstance().savePropertyValue(PropertyKey.CURRENT_USER_LOGIN, LoginModel.ADMIN_TYPE);
                    Utility.showDialogBox(Alert.AlertType.INFORMATION, resultString);
                    startNextStage();
                    break;
                case SUCCESS_CODE_LOGIN_USER_TYPE_USERNAME_PASSWORD_MATCHED:
                    PropertiesUtil.getInstance().savePropertyValue(PropertyKey.CURRENT_USER_LOGIN, LoginModel.USER_TYPE);
                    Utility.showDialogBox(Alert.AlertType.INFORMATION, resultString);
                    startNextStage();
                    break;
            }
        } else if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL) {
            switch (resultMessage.getResultCode()) {
                case FAIL_CODE_LOGIN_SCREEN_INPUT_ISSUE:
                case FAIL_CODE_LOGIN_USERNAME_PASSWORD_NOT_MATCHED:
                    Utility.showDialogBox(Alert.AlertType.ERROR, resultString);
                    break;
            }
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        log.info("handle: " + actionEvent.getSource());
        if (actionEvent.getSource() == bJfxLogin) {
            populateObject();
            new LoginActionPerformed(this, loginModel).actionPerform();
        } else if (actionEvent.getSource() == bJfxClose) {
            ((Stage) apMain.getScene().getWindow()).close();
        }
    }
}
