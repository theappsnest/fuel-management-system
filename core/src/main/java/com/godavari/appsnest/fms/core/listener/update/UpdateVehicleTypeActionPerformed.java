package com.godavari.appsnest.fms.core.listener.update;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.VehicleType;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

/**
 *
 */

@Log4j
public class UpdateVehicleTypeActionPerformed extends BaseActionPerformedListener {

    private VehicleType vehicleType;

    public UpdateVehicleTypeActionPerformed(IOnActionPerformed onActionPerformed, VehicleType vehicleType) {
        super(onActionPerformed);
        this.vehicleType = vehicleType;
    }

    @Override
    public ResultMessage preActionPerformCheck() {
        log.info("preActionPerformCheck");
        if (vehicleType == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_TYPE_INPUT_ISSUE, "");
        }

        String type = StringUtils.isEmpty(vehicleType.getType()) ? null : vehicleType.getType().trim();
        // null or empty
        if (StringUtils.isEmpty(type)) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_TYPE_INPUT_ISSUE, ResourceString.getString("fail_insert_vehicle_type_cant_be_empty"));
        }
        return null;
    }

    @Override
    public void actionPerform() {
        try {
            log.info("actionPerform");
            // check if name already in the database
            if (VehicleType.isRowExistByName(vehicleType.getType())) {
                // fail code
                onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_TYPE_INPUT_ISSUE, ResourceString.getString("fail_insert_vehicle_type_exist_in_db")));
                return;
            }

            // insert no issue
            int updateCount = vehicleType.update();
            log.info("updateCount: " + updateCount);
            // success code
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_UPDATE_VEHICLE_TYPE, ResourceString.getString("success_insert_vehicle_type_inserted_successfully")));
            return;
        } catch (SQLException e) {
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_SQL_EXCEPTION_THROWN, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            // fail code
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_EXCEPTION_THROWN, ResourceString.getString("fail_code_string_exception_thrown")));
        }
    }
}
