package com.godavari.appsnest.fms.ui.frame.list;

import com.godavari.appsnest.fms.ui.frame.BaseApplication;

public class SavedHodManageListFrame extends BaseApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return "layout/list/saved_hod_manage_list_layout.fxml";
    }
}