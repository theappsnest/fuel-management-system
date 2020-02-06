package com.godavari.appsnest.fms.ui.frame;

public class VehicleTypeFrame extends BaseApplication {

    public static final String URL = "layout/old_frame/vehicle_type_frame_layout.fxml";
    public static final String URL_NEW = "layout/new_vehicle_type_frame_layout.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return URL_NEW;
    }
}
