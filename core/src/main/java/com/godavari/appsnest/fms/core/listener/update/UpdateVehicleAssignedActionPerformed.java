package com.godavari.appsnest.fms.core.listener.update;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.model.Vehicle;
import com.godavari.appsnest.fms.dao.model.VehicleAssigned;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;

@Log4j
public class UpdateVehicleAssignedActionPerformed extends BaseActionPerformedListener {

    private VehicleAssigned vehicleAssigned;

    public UpdateVehicleAssignedActionPerformed(IOnActionPerformed onActionPerformed, VehicleAssigned vehicleAssigned) {
        super(onActionPerformed);
        this.vehicleAssigned = vehicleAssigned;
    }

    @Override
    public ResultMessage preActionPerformCheck() {

        log.info("preActionPerformCheck");
        if (vehicleAssigned == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_ASSIGNED_INPUT_ISSUE, "");
        }

        Department department = vehicleAssigned.getDepartment();
        if (department == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_ASSIGNED_INPUT_ISSUE,
                    ResourceString.getString("fail_insert_department_not_selected"));
        }

        Vehicle vehicle = vehicleAssigned.getVehicle();
        if (vehicle == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_ASSIGNED_INPUT_ISSUE, ResourceString.getString("fail_insert_vehicle_no_cant_be_empty"));
        }

        String vehicleNo = StringUtils.isEmpty(vehicle.getVehicleNo()) ? null : vehicle.getVehicleNo().trim();
        if (StringUtils.isEmpty(vehicleNo)) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_ASSIGNED_INPUT_ISSUE, ResourceString.getString("fail_insert_vehicle_no_cant_be_empty"));
        }

        vehicleNo = vehicleNo.toUpperCase();
        if (!Vehicle.isValidVehicleNo(vehicleNo)) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_ASSIGNED_INPUT_ISSUE, ResourceString.getString("fail_insert_invalid_vehicle_no"));
        }

        if (vehicle.getVehicleType() == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_ASSIGNED_INPUT_ISSUE, ResourceString.getString("fail_insert_vehicle_type_not_selected"));
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

            VehicleAssigned vehicleAssignedFromDb = VehicleAssigned.getRowById(vehicleAssigned.getId());

            if (vehicleAssigned.equals(vehicleAssignedFromDb)) {
                // no change return
                onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_ASSIGNED_INPUT_ISSUE, "both vehicle assigned are same"));
                return;
            }

            boolean isVehicleUpdated = !vehicleAssigned.getVehicle().equals(vehicleAssignedFromDb.getVehicle());
            boolean isDepartmentUpdated = !vehicleAssigned.getDepartment().equals(vehicleAssignedFromDb.getDepartment());

            if (!isVehicleUpdated && !isDepartmentUpdated) {
                onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_UPDATE_VEHICLE_ASSIGNED_INPUT_ISSUE,
                        "Nothing to update"));
                return;
            }

            if (isVehicleUpdated) {
                int updateCount = vehicleAssigned.getVehicle().update();
                log.info("Vehicle, updateCount: " + updateCount);
            }
            if (isDepartmentUpdated) {
                VehicleAssigned vehicleAssignedToInsert = new VehicleAssigned();
                vehicleAssignedToInsert.setDepartment(vehicleAssigned.getDepartment());
                vehicleAssignedToInsert.setVehicle(vehicleAssigned.getVehicle());
                vehicleAssignedToInsert.setCurrent(true);
                VehicleAssigned insertedVehicleAssigned = vehicleAssignedToInsert.insert();
                log.info("Successfully inserted, insertedVehicleAssigned: " + insertedVehicleAssigned);

                vehicleAssignedFromDb.setCurrent(false);
                int updateCount = vehicleAssignedFromDb.update();
                log.info("VehicleAssigned, updateCount: " + updateCount);
            }

            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_UPDATE_VEHICLE_ASSIGNED, "Vehicle Manage updated"));
            return;
        } catch (SQLException e) {
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_SQL_EXCEPTION_THROWN, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_EXCEPTION_THROWN, ResourceString.getString("fail_code_string_exception_thrown")));
            return;
        }
    }
}
