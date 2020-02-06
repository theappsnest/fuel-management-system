package com.godavari.appsnest.fms.core.listener.startactivation;

import com.godavari.appsnest.fms.core.ResultMessage;
import com.godavari.appsnest.fms.core.interfaces.IOnActionPerformed;
import com.godavari.appsnest.fms.core.listener.BaseActionPerformedListener;
import com.godavari.appsnest.fms.core.utility.ResourceString;
import com.godavari.appsnest.fms.dao.model.LoginModel;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import static com.godavari.appsnest.fms.core.utility.ActionPerformedSuccessFailCode.*;
import static com.godavari.appsnest.fms.dao.utility.Constants.*;

@Log4j
public class LoginActionPerformed extends BaseActionPerformedListener {

    private LoginModel userInputLoginModel;
    private LoginModel developerAdminLoginModel;
    private LoginModel adminTypeLoginModel;
    private LoginModel userTypeLoginModel;

    public LoginActionPerformed(IOnActionPerformed onActionPerformed, LoginModel userInputLoginModel) {
        super(onActionPerformed);
        this.userInputLoginModel = userInputLoginModel;
    }

    @Override
    public ResultMessage preActionPerformCheck() {
        if (userInputLoginModel == null) {
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_LOGIN_SCREEN_INPUT_ISSUE, "");
        }

        if (StringUtils.isEmpty(userInputLoginModel.getUserName()) || StringUtils.isEmpty(userInputLoginModel.getUserName().trim())) {
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_LOGIN_SCREEN_INPUT_ISSUE,
                    ResourceString.getString("fail_string_login_user_name_cant_empty"));
        }

        if (StringUtils.isEmpty(userInputLoginModel.getPassword()) || StringUtils.isEmpty(userInputLoginModel.getPassword().trim())) {
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_LOGIN_SCREEN_INPUT_ISSUE,
                    ResourceString.getString("fail_string_login_password_cant_empty"));
        }

        if (developerAdminLoginModel == null) {
            return new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_LOGIN_SCREEN_INPUT_ISSUE,
                    ResourceString.getString("fail_string_login_user_developer_file_not_found"));
        }

        return null;
    }

    @Override
    public void actionPerform() {
        try {
            log.debug("actionPerform");

            this.developerAdminLoginModel = LoginModel.getObjectByNameFromResource(DEVELOPER_LOGIN_ADMIN_FILE_NAME);
            this.adminTypeLoginModel = LoginModel.getObjectByName(LOGIN_ADMIN_TYPE_FILE_SAVED_NAME);
            this.userTypeLoginModel = LoginModel.getObjectByName(LOGIN_USER_TYPE_FILE_SAVED_NAME);

            ResultMessage resultMessage = preActionPerformCheck();
            if (resultMessage != null) {
                log.info(resultMessage);
                onActionPerformed.onActionPerformedResult(resultMessage);
                return;
            }

            // if admin type file not found, create one
            if (adminTypeLoginModel == null) {
                adminTypeLoginModel = LoginModel.createFactoryTypeLoginModelFile(LoginModel.ADMIN_TYPE);
            }

            // if user type file not found, create one
            if (userTypeLoginModel == null) {
                userInputLoginModel = LoginModel.createFactoryTypeLoginModelFile(LoginModel.USER_TYPE);
            }

            boolean isDeveloperMatchedWithUserInput = userInputLoginModel.checkUserNameAndPassword(developerAdminLoginModel);
            boolean isAdminTypeMatchWithUserInput = userInputLoginModel.checkUserNameAndPassword(adminTypeLoginModel);
            boolean isUserTypeMatchUserInput = userInputLoginModel.checkUserNameAndPassword(userTypeLoginModel);

            // assign type
            int loginResultCode = 0;
            String loginResultMessage = null;
            if (isUserTypeMatchUserInput) {
                loginResultCode = SUCCESS_CODE_LOGIN_USER_TYPE_USERNAME_PASSWORD_MATCHED;
                loginResultMessage = ResourceString.getString("success_code_login_user_type_username_password_matched");
            }

            if (isAdminTypeMatchWithUserInput) {
                loginResultCode = SUCCESS_CODE_LOGIN_ADMIN_TYPE_USERNAME_PASSWORD_MATCHED;
                loginResultMessage = ResourceString.getString("success_code_login_admin_type_username_password_matched");
            }

            if (isDeveloperMatchedWithUserInput) {
                loginResultCode = SUCCESS_CODE_LOGIN_DEVELOPER_USERNAME_PASSWORD_MATCHED;
                loginResultMessage = ResourceString.getString("success_code_login_developer_username_password_matched");
            }

            if (loginResultCode != 0) {
                onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_SUCCESS, loginResultCode, loginResultMessage));
                return;
            }

            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_LOGIN_USERNAME_PASSWORD_NOT_MATCHED,
                    ResourceString.getString("fail_string_username_password_not_matched")));
            return;
            // fail code
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            onActionPerformed.onActionPerformedResult(new ResultMessage(RESULT_TYPE_FAIL, FAIL_CODE_EXCEPTION_THROWN, ResourceString.getString("fail_code_string_exception_thrown")));
        }
    }
}
