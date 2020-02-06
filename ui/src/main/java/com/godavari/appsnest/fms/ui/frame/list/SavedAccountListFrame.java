package com.godavari.appsnest.fms.ui.frame.list;

import com.godavari.appsnest.fms.ui.frame.BaseApplication;

public class SavedAccountListFrame extends BaseApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return "layout/list/saved_account_list_layout.fxml";
    }
}
