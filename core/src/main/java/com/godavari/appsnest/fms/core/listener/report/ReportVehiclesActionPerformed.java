package com.godavari.appsnest.fms.core.listener.report;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.report.ReportVehiclesModel;
import com.godavari.appsnest.fms.report.GenerateReportVehicles;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.sql.SQLException;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class ReportVehiclesActionPerformed extends BaseReportActionPerformedListener {

    public ReportVehiclesActionPerformed(IOnActionPerformed onActionPerformed, ReportVehiclesModel reportVehiclesModel
            , String fileToBeSaved) {
        super(onActionPerformed, reportVehiclesModel, fileToBeSaved);
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

            ReportVehiclesModel reportVehiclesModel = (ReportVehiclesModel) baseReportModel;
            reportVehiclesModel.getRowFromToDate();

            if (!reportVehiclesModel.isAnyDataAvailableToGenerateReport()) {
                onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_REPORT_VEHICLES_INPUT_ISSUE,
                        ResourceString.getString("no_record_between_selected_date")));
                return;
            }

            new GenerateReportVehicles(new File(fileToBeSaved), reportVehiclesModel);

            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_REPORT_VEHICLES,
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