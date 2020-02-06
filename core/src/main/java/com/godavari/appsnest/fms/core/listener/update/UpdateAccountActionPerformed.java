package com.godavari.appsnest.fms.core.listener.update;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.HodManage;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

/**
 *
 */

@Log4j
public class UpdateAccountActionPerformed extends BaseActionPerformedListener {

    private Account account;

    public UpdateAccountActionPerformed(IOnActionPerformed onActionPerformed, Account account) {
        super(onActionPerformed);
        this.account = account;
    }

    @Override
    public ResultMessage preActionPerformCheck() {
        log.debug("preActionPerformCheck");
        if (account == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE, "");
        }

        LocalDateTime localDateTime = account.getDateTime();
        if (localDateTime == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE, "");
        }

        if (localDateTime.toLocalDate() == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE, "Select Date");
        }

        if (localDateTime.toLocalTime() == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE, "Select Time");
        }

        HodManage hodManage = account.getHodManage();
        if (hodManage == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE, "Select Department");
        }

        /*VehicleAssigned vehicleAssigned = account.getVehicleAssigned();
        if (vehicleAssigned == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_ACCOUNT_INPUT_ISSUE, "Select Vehicle");
        }*/

        double input = account.getInput();
        double output = account.getOutput();

        if (input < 0) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE, "Input cant be negative");
        }

        if (output < 0) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE, "Output cant be negative");
        }

        // both cant be zero at the time
        if (input == 0 && output == 0) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE, "Both input and output cant be empty");
        }

        // both cant be more than zero at a time, only one entry at the tim
        if (input >0 && output>0)
        {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE, "in or out one at a time");
        }
        //todo previous current reading check

        String owner = StringUtils.isEmpty(account.getOwner()) ? null : account.getOwner().trim();
        if (StringUtils.isEmpty(owner)) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_ACCOUNT_INPUT_ISSUE, "Owner cant be empty");
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
//todo account udpate is required not inserted
            int updateCount = account.update();
            log.info("updateCount: " + updateCount);
            // success code
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_UPDATE_ACCOUNT, "Account successfully inserted"));
            return;

        } catch (SQLException e) {
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_SQL_EXCEPTION_THROWN, e.getMessage()));
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            // fail code
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_EXCEPTION_THROWN, ResourceString.getString("fail_code_string_exception_thrown")));
        }
    }
}