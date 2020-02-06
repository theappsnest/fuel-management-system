package com.godavari.appsnest.fms.core.listener.report;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.report.MasterModel;
import com.godavari.appsnest.fms.dao.model.report.ReportMasterModel;
import com.godavari.appsnest.fms.report.GenerateReportMaster;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class ReportMasterActionPerformed extends BaseReportActionPerformedListener {

    public ReportMasterActionPerformed(IOnActionPerformed onActionPerformed, ReportMasterModel reportMasterModel,
                                       String fileToBeSaved) {
        super(onActionPerformed, reportMasterModel, fileToBeSaved);
    }

    @Override
    public void actionPerform() {
        try {
            log.info("actionPerform");
            ResultMessage resultMessage = preActionPerformCheck();
            if (resultMessage != null) {
                onActionPerformed.onActionPerformedResult(resultMessage);
                return;
            }

            ReportMasterModel reportMasterModel = (ReportMasterModel) baseReportModel;
            reportMasterModel.getRowByFromToDate();

            if (!reportMasterModel.isAnyDataAvailableToGenerateReport()) {
                onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_REPORT_MASTER_INPUT_ISSUE,
                        ResourceString.getString("no_record_between_selected_date")));
                return;
            }

            new GenerateReportMaster(new File(fileToBeSaved), reportMasterModel);

            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_REPORT_MASTER,
                    ResourceString.getString("report_generated_successfully")));
            return;
        } catch (SQLException e) {
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_SQL_EXCEPTION_THROWN, e.getMessage()));
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_EXCEPTION_THROWN, ResourceString.getString("fail_code_string_exception_thrown")));
        }
    }
}
