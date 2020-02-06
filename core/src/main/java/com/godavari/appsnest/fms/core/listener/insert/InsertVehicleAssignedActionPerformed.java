package com.godavari.appsnest.fms.core.listener.insert;

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
public class InsertVehicleAssignedActionPerformed extends BaseActionPerformedListener {

    private VehicleAssigned vehicleAssigned;

    public InsertVehicleAssignedActionPerformed(IOnActionPerformed onActionPerformed, VehicleAssigned vehicleAssigned) {
        super(onActionPerformed);
        this.vehicleAssigned = vehicleAssigned;
    }

    @Override
    public ResultMessage preActionPerformCheck() {
        log.info("preActionPerformCheck");

        if (vehicleAssigned == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_VEHICLE_ASSIGNED_INPUT_ISSUE, "");
        }

        Department department = vehicleAssigned.getDepartment();
        if (department == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_VEHICLE_ASSIGNED_INPUT_ISSUE,
                    ResourceString.getString("fail_insert_department_not_selected"));
        }

        Vehicle vehicle = vehicleAssigned.getVehicle();
        if (vehicle == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_VEHICLE_ASSIGNED_INPUT_ISSUE, ResourceString.getString("fail_insert_vehicle_no_cant_be_empty"));
        }

        String vehicleNo = StringUtils.isEmpty(vehicle.getVehicleNo()) ? null : vehicle.getVehicleNo().trim();
        if (StringUtils.isEmpty(vehicleNo)) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_VEHICLE_ASSIGNED_INPUT_ISSUE, ResourceString.getString("fail_insert_vehicle_no_cant_be_empty"));
        }

        vehicleNo = vehicleNo.toUpperCase();
        if (!Vehicle.isValidVehicleNo(vehicleNo)) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_VEHICLE_ASSIGNED_INPUT_ISSUE, ResourceString.getString("fail_insert_invalid_vehicle_no"));
        }

        if (vehicle.getVehicleType() == null) {
            // fail code
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_VEHICLE_ASSIGNED_INPUT_ISSUE, ResourceString.getString("fail_insert_vehicle_type_not_selected"));
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

            String vehicleNo = vehicleAssigned.getVehicle().getVehicleNo();
            Vehicle vehicleFromDb = Vehicle.getRowByName(vehicleNo);
            if (vehicleFromDb != null) {
                if (VehicleAssigned.isRowExistByVehicleIdAndCurrent(vehicleFromDb.getId(), true)) {
                    // fail code
                    onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_INSERT_VEHICLE_ASSIGNED_INPUT_ISSUE, ResourceString.getString("fail_insert_vehicle_no_exist_in_db")));
                    return;
                }
            }

            Vehicle saveVehicle = null;
            if (vehicleFromDb != null) {
                saveVehicle = vehicleFromDb;
            } else {
                saveVehicle = new Vehicle(vehicleNo, vehicleAssigned.getVehicle().getVehicleType());
                saveVehicle.insert();
                System.out.println("successfully inserted, saveVehicle: " + saveVehicle);
            }

            // now insert vehicleAssigned in vehicle_assigned table, since we got the vehicle no after inserting in vehicle table
            // selected department will have new vehicle, so currently active in that particular department
            VehicleAssigned vehicleAssignedToSave = new VehicleAssigned();
            vehicleAssignedToSave.setVehicle(saveVehicle);
            vehicleAssignedToSave.setDepartment(vehicleAssigned.getDepartment());
            vehicleAssignedToSave.setCurrent(true);
            VehicleAssigned insertedVehicleAssigned = vehicleAssignedToSave.insert();
            System.out.println("successfully inserted, vehicleAssigned: " + insertedVehicleAssigned);
            //success code
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_INSERT_VEHICLE_ASSIGNED, ResourceString.getString("success_insert_vehicle_assigned_inserted_successful")));
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
