package com.godavari.appsnest.fms.ui.frame.combobox;

import com.godavari.appsnest.fms.ui.frame.BaseApplication;

public class SavedVehicleTypeComboBoxFrame extends BaseApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return "layout/combobox/saved_vehicle_type_combo_box_layout.fxml";
    }
}
