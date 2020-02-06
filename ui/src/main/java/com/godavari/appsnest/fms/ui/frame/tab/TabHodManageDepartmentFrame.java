package com.godavari.appsnest.fms.ui.frame.tab;

import com.godavari.appsnest.fms.ui.frame.BaseApplication;

public class TabHodManageDepartmentFrame extends BaseApplication {

    public static final String URL = "layout/tab/tab_hod_manage_department_frame_layout.fxml";
    public static final String URL_NEW = "layout/tab/new_tab_hod_manage_department_frame_layout.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return URL_NEW;
    }
}
