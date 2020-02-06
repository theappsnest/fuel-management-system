package com.godavari.appsnest.fms.ui.frame.startactivation;

import com.godavari.appsnest.fms.ui.frame.BaseApplication;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ActivationScreenFrame extends BaseApplication {

    public static final String URL = "layout/startactivation/activation_screen_frame_layout.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
        super.start(stage);
    }

    @Override
    public String getFxmlPath() {
        return URL;
    }
}
