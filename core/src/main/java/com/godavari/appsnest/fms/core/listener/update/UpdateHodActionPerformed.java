package com.godavari.appsnest.fms.core.listener.update;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;


@Log4j
public class UpdateHodActionPerformed extends BaseActionPerformedListener {

    private HeadOfDepartment headOfDepartment;

    public UpdateHodActionPerformed(IOnActionPerformed onActionPerformed, HeadOfDepartment headOfDepartment) {
        super(onActionPerformed);
        this.headOfDepartment = headOfDepartment;
    }

    @Override
    public ResultMessage preActionPerformCheck() {
        log.info("preActionPerformCheck");
        if (headOfDepartment == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_HEAD_OF_DEPARTMENT_INPUT_ISSUE, ResourceString.getString("fail_code_insert_hod_object_cant_null"));
        }

        String name = StringUtils.isEmpty(headOfDepartment.getName()) ? null : headOfDepartment.getName().trim();
        // null or empty
        if (StringUtils.isEmpty(name)) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_HEAD_OF_DEPARTMENT_INPUT_ISSUE, ResourceString.getString("fail_code_insert_hod_name_cant_empty"));
        }

        return null;
    }

    @Override
    public void actionPerform() {
        try {
            log.info("actionPerform");
            ResultMessage preCheckResultMessage = preActionPerformCheck();
            if (preCheckResultMessage != null) {
                onActionPerformed.onActionPerformedResult(preCheckResultMessage);
                return;
            }

            // check if name already in the database
            if (HeadOfDepartment.isRowExistByName(headOfDepartment.getName())) {
                // fail code
                onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_HEAD_OF_DEPARTMENT_INPUT_ISSUE, ResourceString.getString("fail_code_insert_hod_name_exist_in_db")));
                return;
            }

            // insert no issue
            //todo update the name trim value back to the object for saving purpose, otherwise it is a local copy
            int updateCount = headOfDepartment.update();
            log.info("updateCount: "+updateCount);

            // success code
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_UPDATE_HEAD_OF_DEPARTMENT, ResourceString.getString("hod_name_inserted_successfully")));
            return;
        } catch (SQLException e) {
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_SQL_EXCEPTION_THROWN, e.getMessage()));
        }catch (Exception e) {
            log.error(e);
            // fail code
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_EXCEPTION_THROWN, ResourceString.getString("fail_code_string_exception_thrown")));
        }
    }
}
