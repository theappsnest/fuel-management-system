package com.godavari.appsnest.fms.core.listener.delete;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.HodManage;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class DeleteHodManageActionPerformed extends BaseActionPerformedListener {

    private HodManage hodManage;

    public DeleteHodManageActionPerformed(IOnActionPerformed onActionPerformed, HodManage hodManage) {
        super(onActionPerformed);
        this.hodManage = hodManage;
    }

    @Override
    public ResultMessage preActionPerformCheck() {
        log.debug("preActionPerformCheck");
        if (hodManage == null) {
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_DELETE_HOD_MANAGE_ISSUE, ResourceString.getString("fail_String_delete_hod_manage_not_selected"));
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

            //todo delete from department when no entries are available
            // very important

            int deleteCount = hodManage.delete();
            log.info("deleteCount: "+deleteCount);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_DELETE_HOD_MANAGE, ResourceString.getString("success_string_delete_hod_manage")));
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
