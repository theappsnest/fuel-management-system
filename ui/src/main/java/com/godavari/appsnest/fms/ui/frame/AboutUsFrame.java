package com.godavari.appsnest.fms.ui.frame;

public class AboutUsFrame extends BaseApplication {

    public static final String URL = "layout/about_us_frame_layout.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return URL;
    }
}
