package com.godavari.appsnest.fms.core.listener.startactivation;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.MD5Model;
import lombok.extern.log4j.Log4j;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;
import static com.godavari.appsnest.fms.dao.utility.Constants.*;

@Log4j
public class StartScreenProcessActionPerformed extends BaseActionPerformedListener {

    private MD5Model adminFile;
    private MD5Model userFileSaved;
    private MD5Model userFileGenerated;

    public StartScreenProcessActionPerformed(IOnActionPerformed onActionPerformed) {
        super(onActionPerformed);
    }

    @Override
    public ResultMessage preActionPerformCheck() {

        if (adminFile == null) {
            // exe not prepared to give to customer, blocking screen contact admin
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_ADMIN_FILE_NOT_FOUND, ResourceString.getString("fail_string_start_screen_admin_file_not_found"));
        }

        if (userFileGenerated == null) {
            // exe not prepared to give to customer, blocking screen contact admin
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_USER_FILE_GENERATED_NOT_FOUND, ResourceString.getString("fail_string_start_screen_user_file_generated_not_found"));
        }

        if (userFileSaved == null) {
            // not activated
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_USER_FILE_SAVED_NOT_FOUND, ResourceString.getString("fail_string_start_screen_user_file_saved_not_found"));
        }
        return null;
    }

    @Override
    public void actionPerform() {
        log.info("actionPerform");
        adminFile = MD5Model.getObjectByNameFromResource(ADMIN_FILE_NAME);
        userFileSaved = MD5Model.getObjectByName(USER_FILE_SAVED_NAME);
        userFileGenerated = MD5Model.getObjectByNameFromResource(USER_FILE_GENERATED_NAME);

        ResultMessage resultMessage = preActionPerformCheck();
        if (resultMessage != null) {
            onActionPerformed.onActionPerformedResult(resultMessage);
            return;
        }

        boolean isAdminMatchedWithUserInput = adminFile.checkSecurePassword(userFileSaved);
        boolean isUserGeneratedMatchedWithUserInput = userFileGenerated.checkSecurePassword(userFileSaved);

        if (isUserGeneratedMatchedWithUserInput) {
            onActionPerformed.onActionPerformedResult(
                    new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_START_SCREEN_USER_GENERATED_AND_SAVED_PASSWORD_MATCHED, ResourceString.getString("success_string_start_screen_user_generated_and_saved_password_matched")));
            return;
        }

        if (isAdminMatchedWithUserInput) {
            onActionPerformed.onActionPerformedResult(
                    new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_START_SCREEN_ADMIN_AND_USER_SAVED_PASSWORD_MATCHED, ResourceString.getString("success_string_start_screen_admin_and_user_saved_password_matched")));
            return;
        }

        // store password doesnt match, activation fails, show activation screen
        userFileSaved.delete(USER_FILE_SAVED_NAME);
        onActionPerformed.onActionPerformedResult(
                new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_ADMIN_USER_GENERATED_AND_SAVED_SECURE_PASSWORD_NOT_MATCH, ResourceString.getString("fail_string_start_screen_user_generated_and_saved_password_not_match")));
        return;
    }
}
