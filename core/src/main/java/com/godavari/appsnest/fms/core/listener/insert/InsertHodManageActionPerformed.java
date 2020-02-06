package com.godavari.appsnest.fms.core.listener.insert;

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

/**
 * responsible to add row in dept and hodmanage table
 */

@Log4j
public class InsertHodManageActionPerformed extends BaseActionPerformedListener {

    private HodManage hodManage;

    public InsertHodManageActionPerformed(IOnActionPerformed onActionPerformed, HodManage hodManage) {
        super(onActionPerformed);
        this.hodManage = hodManage;
    }

    @Override
    public ResultMessage preActionPerformCheck() {
        log.info("preActionPerformCheck");
        if (hodManage == null) {
            //fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_HOD_MANAGE_INPUT_ISSUE, "");
        }

        Department department = hodManage.getDepartment();
        if (department == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_HOD_MANAGE_INPUT_ISSUE, ResourceString.getString("department_name_cant_be_empty"));

        }

        String departmentName = StringUtils.isEmpty(department.getName()) ? null : department.getName().trim();
        if (StringUtils.isEmpty(departmentName)) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_HOD_MANAGE_INPUT_ISSUE, ResourceString.getString("department_name_cant_be_empty"));
        }

        HeadOfDepartment headOfDepartment = hodManage.getHeadOfDepartment();
        if (headOfDepartment == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_HOD_MANAGE_INPUT_ISSUE, ResourceString.getString("hod_name_not_selected"));
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

            String departmentName = hodManage.getDepartment().getName();
            Department departmentFromDb = Department.getRowByName(departmentName);
            if (departmentFromDb != null) {
                if (HodManage.isRowExistByDeptIdAndCurrent(departmentFromDb.getId(), true)) {
                    // fail code
                    onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_HOD_MANAGE_INPUT_ISSUE, ResourceString.getString("department_name_exist_in_db")));
                    return;
                }
            }

            Department saveDepartment = null;
            if (departmentFromDb != null) {
                saveDepartment = departmentFromDb;
            } else {
                // insert the new department in department table
                saveDepartment = new Department(departmentName);
                saveDepartment.insert();
                System.out.println("inserted successfully, saveDepartment: " + saveDepartment);
            }

            // now insert hodManage in hod_manage table, since we got the dept_id after inserting the department
            // selected hod will be the hod of the new department, so current true
            HodManage saveHodManage = new HodManage(saveDepartment, hodManage.getHeadOfDepartment());
            saveHodManage.setCurrent(true);
            HodManage insertedHodManage = saveHodManage.insert();
            System.out.println("inserted successfully, hodManage: " + saveHodManage);
            //success code
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_INSERT_HOD_MANAGE, ResourceString.getString("hod_manage_inserted_successfully")));
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
