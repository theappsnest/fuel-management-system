package com.godavari.appsnest.fms.core.listener.report;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.report.Master1Model;
import com.godavari.appsnest.fms.dao.model.report.ReportMaster1Model;
import com.godavari.appsnest.fms.report.GenerateReportMaster1;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class ReportMaster1ActionPerformed extends BaseReportActionPerformedListener {


    public ReportMaster1ActionPerformed(IOnActionPerformed onActionPerformed, ReportMaster1Model reportMaster1Model,
                                        String fileToBeSaved) {
        super(onActionPerformed, reportMaster1Model, fileToBeSaved);
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

            ReportMaster1Model reportMaster1Model = (ReportMaster1Model) baseReportModel;
            reportMaster1Model.getRowByFromToDate();

            if (!reportMaster1Model.isAnyDataAvailableToGenerateReport()) {
                onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_REPORT_MASTER1_INPUT_ISSUE,
                        ResourceString.getString("no_record_between_selected_date")));
                return;
            }

            new GenerateReportMaster1(new File(fileToBeSaved), reportMaster1Model);

            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_REPORT_MASTER1,
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
