package com.godavari.appsnest.fms.ui.controller.report;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.listener.report.ReportDepartmentsActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.dao.model.report.ReportDepartmentsModel;
import com.godavari.appsnest.fms.ui.utility.ResourceString;
import com.godavari.appsnest.fms.ui.utility.Utility;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class ReportDepartmentsFrameController extends BaseReportController<ReportDepartmentsFrameController> {

    private ReportDepartmentsModel reportDepartmentsModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("initialize");
        super.initialize(url, resourceBundle);
        reportType = ResourceString.getString("report_departments");
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        log.info("onActionPerformedResult, resultMessage: " + resultMessage);
        super.onActionPerformedResult(resultMessage);
        String resultString = resultMessage.getResultString();
        if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_SUCCESS) {
            switch (resultMessage.getResultCode()) {
                case ActionPerformedSuccessFailCode.SUCCESS_CODE_REPORT_DEPARTMENTS:
                    updateUi(null);
                    Utility.showDialogBox(Alert.AlertType.INFORMATION, resultString);
                    break;
            }
        } else if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL) {
            switch (resultMessage.getResultCode()) {
                case ActionPerformedSuccessFailCode.FAIL_CODE_REPORT_DEPARTMENTS_INPUT_ISSUE:
                    Utility.showDialogBox(Alert.AlertType.ERROR, resultString);
                    break;
            }
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        log.info("handle, actionEvent: " + actionEvent.getSource());
        if (actionEvent.getSource() == jfxBReset) {
            updateUi(null);
        } else if (actionEvent.getSource() == jfxBGenerateReport) {
            populateObject();
            new ReportDepartmentsActionPerformed(this, reportDepartmentsModel, absoluteReportFilePathWithExtension).actionPerform();
        }
    }

    @Override
    protected void populateObject() {
        reportDepartmentsModel = new ReportDepartmentsModel();
        reportDepartmentsModel.setFromLocalDate(jfxDPFromDate.getValue());
        reportDepartmentsModel.setToLocalDate(jfxDPToDate.getValue());
    }
}
