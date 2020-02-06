package com.godavari.appsnest.fms.core.listener.report;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.report.BaseReportModel;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public abstract class BaseReportActionPerformedListener extends BaseActionPerformedListener {

    protected BaseReportModel baseReportModel;

    protected String fileToBeSaved;

    public BaseReportActionPerformedListener(IOnActionPerformed onActionPerformed, BaseReportModel baseReportModel,
                                             String fileToBeSaved) {
        super(onActionPerformed);
        this.baseReportModel = baseReportModel;
        this.fileToBeSaved = fileToBeSaved;
    }

    @Override
    public ResultMessage preActionPerformCheck() {
        log.info("preActionPerformCheck");
        if (baseReportModel.getFromLocalDate() == null) {
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_BASE_REPORT_INPUT_ISSUE,
                    ResourceString.getString("select_from_date"));
        }

        if (baseReportModel.getToLocalDate() == null) {
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_BASE_REPORT_INPUT_ISSUE,
                    ResourceString.getString("select_to_date"));
        }

        if (StringUtils.isEmpty(fileToBeSaved) || StringUtils.isEmpty(fileToBeSaved.trim())) {
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_BASE_REPORT_INPUT_ISSUE,
                    ResourceString.getString("select_directory_to_save_file"));
        }

        return null;
    }
}
