package com.godavari.appsnest.fms.ui.frame.treeview;

import com.godavari.appsnest.fms.ui.frame.BaseApplication;

public class SavedVehicleAssignedTreeViewFrame extends BaseApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return "layout/treeview/vehicle_assigned_tree_view_layout.fxml";
    }
}
