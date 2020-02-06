package com.godavari.appsnest.fms.ui.frame.custom;

import com.godavari.appsnest.fms.ui.frame.BaseApplication;

public class CurrentReadingFrame extends BaseApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return "layout/custom/current_reading_layout.fxml";
    }
}
