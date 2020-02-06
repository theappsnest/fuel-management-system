package com.godavari.appsnest.fms.core.listener.update;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.model.HeadOfDepartment;
import com.godavari.appsnest.fms.dao.model.HodManage;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class UpdateHodManageActionPerformed extends BaseActionPerformedListener {

    private HodManage hodManage;

    public UpdateHodManageActionPerformed(IOnActionPerformed onActionPerformed, HodManage hodManage) {
        super(onActionPerformed);
        this.hodManage = hodManage;
    }

    @Override
    public ResultMessage preActionPerformCheck() {

        log.info("preActionPerformCheck");
        if (hodManage == null) {
            //fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_HOD_MANAGE_INPUT_ISSUE, "");
        }

        Department department = hodManage.getDepartment();
        if (department == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_HOD_MANAGE_INPUT_ISSUE, ResourceString.getString("department_name_cant_be_empty"));
        }

        String departmentName = department.getName().trim();
        if (StringUtils.isEmpty(departmentName)) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_HOD_MANAGE_INPUT_ISSUE, ResourceString.getString("department_name_cant_be_empty"));
        }

        HeadOfDepartment headOfDepartment = hodManage.getHeadOfDepartment();
        if (headOfDepartment == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_HOD_MANAGE_INPUT_ISSUE, ResourceString.getString("hod_name_not_selected"));
        }

        return null;
    }

    @Override
    public void actionPerform() {
        try {
            log.info("actionPerform");

            log.info("actionPerform");
            ResultMessage preCheckResultMessage = preActionPerformCheck();
            if (preCheckResultMessage != null) {
                onActionPerformed.onActionPerformedResult(preCheckResultMessage);
                return;
            }

            HodManage hodManageFromDb = HodManage.getRowById(hodManage.getId());

            if (hodManage.equals(hodManageFromDb)) {
                // no change return
                onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_HOD_MANAGE_INPUT_ISSUE, "both hod manage are same"));
                return;
            }

            boolean isDepartmentUpdated = true;
            boolean isHodUpdated = true;
            if (hodManage.getDepartment().equals(hodManageFromDb.getDepartment())) {
                // no change in department
                isDepartmentUpdated = false;
            }

            if (hodManageFromDb.getHeadOfDepartment().equals(hodManage.getHeadOfDepartment())) {
                // hod not change return
                isHodUpdated = false;
            }

            // nothing to update
            if (!isDepartmentUpdated && !isHodUpdated) {
                onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_HOD_MANAGE_INPUT_ISSUE,
                        "Nothing to update"));
                return;
            }

            if (isDepartmentUpdated) {// update the department
                int updateCount= hodManage.getDepartment().update();
                log.info("Department, updateCount: "+updateCount);
            }

            if (isHodUpdated) {// update the hod
                HodManage hodManageToInsert = new HodManage();
                hodManageToInsert.setHeadOfDepartment(hodManage.getHeadOfDepartment());
                hodManageToInsert.setDepartment(hodManage.getDepartment());
                hodManageToInsert.setCurrent(true);
                HodManage insertedHodManage = hodManageToInsert.insert();
                log.info("successfully inserted, insertedHodManage"+insertedHodManage);

                hodManageFromDb.setCurrent(false);
                int updateCount = hodManageFromDb.update();
                log.info("HodManage, updateCount: "+updateCount);
            }

            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_UPDATE_HOD_MANAGE, "HOD Manage updated"));
            return;
        } catch (SQLException e) {
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_SQL_EXCEPTION_THROWN, e.getMessage()));
        } catch (Exception e) {
            log.error(e.getStackTrace());
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_EXCEPTION_THROWN, ResourceString.getString("fail_code_string_exception_thrown")));
            return;
        }
    }
}
