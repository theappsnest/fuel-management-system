package com.godavari.appsnest.fms.ui.frame.startactivation;

import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.ui.frame.BaseApplication;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class StartScreenFrame extends BaseApplication {

    public static final String URL = "layout/startactivation/start_screen_frame_layout.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.getIcons().add(new Image("image/company_logo.png"));
        stage.setTitle(ResourceString.getString("application_name"));
        stage.initStyle(StageStyle.UNDECORATED);
        super.start(stage);
    }

    @Override
    public String getFxmlPath() {
        return URL;
    }
}