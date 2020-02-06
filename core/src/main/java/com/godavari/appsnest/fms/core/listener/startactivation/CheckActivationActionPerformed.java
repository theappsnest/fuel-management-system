package com.godavari.appsnest.fms.core.listener.startactivation;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.MD5Model;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;
import static com.godavari.appsnest.fms.dao.utility.Constants.*;

@Log4j
public class CheckActivationActionPerformed extends BaseActionPerformedListener {

    private MD5Model userInputMD5Model;
    private MD5Model userGeneratedMD5Model;
    private MD5Model adminMD5Model;

    public CheckActivationActionPerformed(IOnActionPerformed onActionPerformed, MD5Model userInputMD5Model) {
        super(onActionPerformed);
        this.userInputMD5Model = userInputMD5Model;
    }

    @Override
    public ResultMessage preActionPerformCheck() {

        if (adminMD5Model == null) {
            // todo something here
            // take some action
            //onActionPerformed.onActionPerformedResult(RESULT_TYPE_FAIL, FAIL_CODE_CONSUMER_INPUT_ISSUE, FAIL_STRING_CONSUMER_NO_CANT_EMPTY);
            //return;
        }

        if (userGeneratedMD5Model == null) {
            // todo something here
        }

        String userPasswordToHash = userInputMD5Model.getPasswordToHash();
        if (StringUtils.isEmpty(userPasswordToHash)) {
            // password to hash cant be empty
            log.warn("userPasswordToHash cant be empty");
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_ACTIVATION_USER_INPUT_ISSUE, ResourceString.getString("fail_string_activation_user_password_key_empty"));
        }

        byte[] salt = userInputMD5Model.getSalt();
        for (int i = 0; i < salt.length; i++) {
            if (salt[i] == -1) {
                // salt cant be empty
                log.warn("input salt index: "+ i+", cant be empty");
                return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_ACTIVATION_USER_INPUT_ISSUE, ResourceString.getString("fail_string_activation_user_password_key_empty"));
            }
        }
        return null;
    }

    @Override
    public void actionPerform() {

        log.info("actionPerform");

        this.userGeneratedMD5Model = MD5Model.getObjectByNameFromResource(USER_FILE_GENERATED_NAME);
        this.adminMD5Model = MD5Model.getObjectByNameFromResource(ADMIN_FILE_NAME);

        ResultMessage resultMessage = preActionPerformCheck();
        if (resultMessage!=null)
        {
                onActionPerformed.onActionPerformedResult(resultMessage);
                return;
        }

        boolean isAdminMatchedWithUserInput = adminMD5Model.checkSecurePassword(userInputMD5Model);
        boolean isUserGeneratedMatchedWithUserInput = userGeneratedMD5Model.checkSecurePassword(userInputMD5Model);

        if (isUserGeneratedMatchedWithUserInput) {
            userInputMD5Model.save(USER_FILE_SAVED_NAME);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_ACTIVATION_USER_SECURE_PASSWORD_MATCHED, ResourceString.getString("success_string_activation_user_secure_password_matched")));
            return;
        }

        if (isAdminMatchedWithUserInput) {
            adminMD5Model.save(USER_FILE_SAVED_NAME);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, SUCCESS_CODE_ACTIVATION_ADMIN_SECURE_PASSWORD_MATCHED,ResourceString.getString("success_string_activation_admin_secure_password_matched")));
            return;
        }

        // hashed user and salt doesnt match with the generated one
        userInputMD5Model.delete(USER_FILE_SAVED_NAME);
        onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_ACTIVATION_USER_INPUT_ISSUE, ResourceString.getString("fail_string_activation_user_secure_password_doesnt_match")));
    }
}
