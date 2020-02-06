package com.godavari.appsnest.fms.core.listener.delete;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class DeleteHodActionPerformed extends BaseActionPerformedListener {

    private HeadOfDepartment headOfDepartment;

    public DeleteHodActionPerformed(IOnActionPerformed onActionPerformed, HeadOfDepartment headOfDepartment) {
        super(onActionPerformed);
        this.headOfDepartment = headOfDepartment;
    }

    @Override
    public ResultMessage preActionPerformCheck() {
        log.debug("preActionPerformCheck");
        if (headOfDepartment == null) {
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_DELETE_HEAD_OF_DEPARTMENT_ISSUE, ResourceString.getString("fail_string_delete_hod_name_not_selected"));
        }
            return null;
    }

    @Override
    public void actionPerform() {
        try {
            log.debug("actionPerform");
            ResultMessage preCheckResultMessage = preActionPerformCheck();
            if (preCheckResultMessage != null) {
                log.info(preCheckResultMessage);
                onActionPerformed.onActionPerformedResult(preCheckResultMessage);
                return;
            }

            int deleteCount = headOfDepartment.delete();
            log.info("deleteCount: "+deleteCount);
            onActionPerformed.onActionPerformedResult(new
                    ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_DELETE_HEAD_OF_DEPARTMENT, ResourceString.getString("success_string_delete_hod_successful")));
            return;
        }catch (SQLException e) {
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_SQL_EXCEPTION_THROWN, e.getMessage()));
        } catch (Exception e) {
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_EXCEPTION_THROWN, ResourceString.getString("fail_code_string_exception_thrown")));
        }
    }
}
