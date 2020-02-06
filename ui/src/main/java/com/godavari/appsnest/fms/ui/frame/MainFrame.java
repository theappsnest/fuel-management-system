package com.godavari.appsnest.fms.ui.frame;

public class MainFrame extends BaseApplication {

    public static final String URL = "layout/main_frame_layout.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return URL;
    }
}
