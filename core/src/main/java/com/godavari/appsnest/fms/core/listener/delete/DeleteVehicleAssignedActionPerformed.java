package com.godavari.appsnest.fms.core.listener.delete;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.VehicleAssigned;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class DeleteVehicleAssignedActionPerformed extends BaseActionPerformedListener {

    private VehicleAssigned vehicleAssigned;

    public DeleteVehicleAssignedActionPerformed(IOnActionPerformed onActionPerformed, VehicleAssigned vehicleAssigned) {
        super(onActionPerformed);
        this.vehicleAssigned = vehicleAssigned;
    }

    @Override
    public ResultMessage preActionPerformCheck() {
        log.debug("preActionPerformCheck");
        if (vehicleAssigned == null) {
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_DELETE_VEHICLE_ASSIGNED_ISSUE, ResourceString.getString("fail_string_delete_vehicle_assigned_not_selected"));
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

            // todo should do vehicle delete
            // very important

            int deleteCount = vehicleAssigned.delete();
            log.info("deleteCount: "+deleteCount);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_DELETE_VEHICLE_ASSIGNED, ResourceString.getString("success_string_delete_vehicle_assigned_deleted_successful")));
            return;
        } catch (SQLException e) {
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_SQL_EXCEPTION_THROWN, e.getMessage()));
        }catch (Exception e) {
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_EXCEPTION_THROWN, ResourceString.getString("fail_code_string_exception_thrown")));
        }
    }


}
