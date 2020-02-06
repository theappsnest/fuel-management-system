package com.godavari.appsnest.fms.ui.frame.combobox;

import com.godavari.appsnest.fms.ui.frame.BaseApplication;

public class SavedDepartmentComboBoxFrame extends BaseApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return "layout/combobox/saved_department_combo_box_layout.fxml";
    }
}
