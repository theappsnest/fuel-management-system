package com.godavari.appsnest.fms.ui.controller.startactivation;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.listener.startactivation.StartScreenProcessActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.ui.controller.BaseFrameController;
import com.godavari.appsnest.fms.ui.frame.startactivation.ActivationScreenFrame;
import com.godavari.appsnest.fms.ui.frame.startactivation.LoginScreenFrame;
import com.godavari.appsnest.fms.ui.utility.Utility;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class StartScreenFrameController extends BaseFrameController {

    @FXML
    private Label lApplicationName;

    @FXML
    private AnchorPane apMain;

    private FadeTransition fadeIn;
    private FadeTransition fadeOut;

    private Parent nextParentView;
    private String nextParentURLString;

    private Stage secondaryStage = new Stage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("initialize");
        super.initialize(url, resourceBundle);

        animateFadeIn();
        animateFadeOut();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new StartScreenProcessActionPerformed(StartScreenFrameController.this).actionPerform();
            }
        }).start();
    }

    @Override
    protected void updateUi(Object object) {

    }

    @Override
    protected void populateObject() {

    }

    private void animateFadeIn() {
        log.info("animateFadeIn");
        //Start splash with fade in effect
        fadeIn = new FadeTransition(Duration.seconds(1.5), apMain);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();
        fadeIn.setOnFinished((e) -> {
            fadeOut.play();
        });
    }

    private void animateFadeOut() {
        log.info("animateFadeOut");
        //Finish splash with fade out effect
        fadeOut = new FadeTransition(Duration.seconds(1), apMain);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(1);
        fadeOut.setCycleCount(1);
        fadeOut.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (nextParentURLString != null) {
                    log.info("animateFadeOut setOnFinished");
                    nextParentView = Utility.getParent(nextParentURLString);
                    secondaryStage.setScene(new Scene(nextParentView));
                    secondaryStage.initStyle(StageStyle.UNDECORATED);

                    //close the start screen
                    Stage stage = (Stage) apMain.getScene().getWindow();
                    stage.close();

                    //open the next screen
                    secondaryStage.show();
                }
            }
        });
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        if (ActionPerformedSuccessFailCode.RESULT_TYPE_SUCCESS == resultMessage.getResultType()) {
            switch (resultMessage.getResultCode()) {
                case SUCCESS_CODE_START_SCREEN_ADMIN_AND_USER_SAVED_PASSWORD_MATCHED:
                case SUCCESS_CODE_START_SCREEN_USER_GENERATED_AND_SAVED_PASSWORD_MATCHED:
                    nextParentURLString = LoginScreenFrame.URL;
                    break;
            }
        } else if (ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL == resultMessage.getResultType()) {
            switch (resultMessage.getResultCode()) {
                case FAIL_CODE_ADMIN_FILE_NOT_FOUND:
                    //todo hwew
                    //nextParentURL = MainFrame.getFxmlUrl();
                    break;
                case FAIL_CODE_USER_FILE_GENERATED_NOT_FOUND:
                    //
                    break;
                case FAIL_CODE_USER_FILE_SAVED_NOT_FOUND:
                case FAIL_CODE_ADMIN_USER_GENERATED_AND_SAVED_SECURE_PASSWORD_NOT_MATCH:
                    secondaryStage.initStyle(StageStyle.UNDECORATED);
                    nextParentURLString = ActivationScreenFrame.URL;
                    break;
            }
        }
    }

    @Override
    public void handle(Event event){
    }
}

