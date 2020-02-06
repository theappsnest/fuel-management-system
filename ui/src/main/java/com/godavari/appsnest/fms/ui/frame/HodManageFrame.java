package com.godavari.appsnest.fms.ui.frame;

public class HodManageFrame extends BaseApplication {

    public static final String URL = "layout/old_frame/hod_manage_frame_layout.fxml";
    public static final String URL_NEW = "layout/new_hod_manage_frame_layout.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return URL_NEW;
    }
}
