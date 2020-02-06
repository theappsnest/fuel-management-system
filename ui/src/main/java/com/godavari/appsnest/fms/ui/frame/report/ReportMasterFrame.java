package com.godavari.appsnest.fms.ui.frame.report;

import com.godavari.appsnest.fms.ui.frame.BaseApplication;

public class ReportMasterFrame extends BaseApplication {

    public static final String URL = "layout/old_report/report_master_frame_layout.fxml";
    public static final String URL_NEW = "layout/report/new_report_master_frame_layout.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getFxmlPath() {
        return URL_NEW;
    }
}
