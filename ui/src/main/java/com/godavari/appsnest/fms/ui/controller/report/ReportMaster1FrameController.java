package com.godavari.appsnest.fms.ui.controller.report;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.listener.report.ReportMaster1ActionPerformed;
import com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode;
import com.godavari.appsnest.fms.dao.model.report.ReportMaster1Model;
import com.godavari.appsnest.fms.ui.utility.ResourceString;
import com.godavari.appsnest.fms.ui.utility.Utility;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import lombok.extern.log4j.Log4j;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class ReportMaster1FrameController extends BaseReportController<ReportMaster1FrameController> {

    private ReportMaster1Model reportMaster1Model;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("initialize");
        super.initialize(url, resourceBundle);
        reportType = ResourceString.getString("report_master1");
    }

    @Override
    public void onActionPerformedResult(ResultMessage resultMessage) {
        log.info("onActionPerformedResult, resultMessage: " + resultMessage);
        super.onActionPerformedResult(resultMessage);
        String resultString = resultMessage.getResultString();
        if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_SUCCESS) {
            switch (resultMessage.getResultCode()) {
                case ActionPerformedSuccessFailCode.SUCCESS_CODE_REPORT_MASTER1:
                    updateUi(null);
                    Utility.showDialogBox(Alert.AlertType.INFORMATION, resultString);
                    break;
            }
        } else if (resultMessage.getResultType() == ActionPerformedSuccessFailCode.RESULT_TYPE_FAIL) {
            switch (resultMessage.getResultCode()) {
                case ActionPerformedSuccessFailCode.FAIL_CODE_REPORT_MASTER1_INPUT_ISSUE:
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
            new ReportMaster1ActionPerformed(this, reportMaster1Model, absoluteReportFilePathWithExtension).actionPerform();
        }
    }

    @Override
    protected void populateObject() {
        reportMaster1Model = new ReportMaster1Model();
        reportMaster1Model.setFromLocalDate(jfxDPFromDate.getValue());
        reportMaster1Model.setToLocalDate(jfxDPToDate.getValue());
    }
}
