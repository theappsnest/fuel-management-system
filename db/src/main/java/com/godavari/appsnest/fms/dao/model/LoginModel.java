package com.godavari.appsnest.fms.dao.model;

import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.interfaces.ILoginModelDao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import static com.godavari.appsnest.fms.dao.utility.Constants.*;

@Log4j
@Getter
@Setter
public class LoginModel {

    private static final String ADMIN_TYPE_USER_NAME = "admin";
    private static final String USER_TYPE_USER_NAME = "user";

    private static final String ADMIN_TYPE_PASSWORD = "admin";
    private static final String USER_TYPE_PASSWORD = "user";

    public static final String ADMIN_TYPE = "ADMIN";
    public static final String USER_TYPE = "USER";

    private String userName;
    private String password;
    private String type;

    public LoginModel(String userName, String password) {
        this(userName, password, "");
    }

    public LoginModel(String userName, String password, String type) {
        this.userName = userName;
        this.password = password;
        this.type = type;
    }

    public static void main(String[] args) {
        generateDeveloperFile();
    }

    private static void generateDeveloperFile()
    {
        LoginModel loginModel = new LoginModel("developer", "developer");
        loginModel.save(DEVELOPER_LOGIN_ADMIN_FILE_NAME);
    }

    public static LoginModel createFactoryTypeLoginModelFile(String type) {
        LoginModel loginModel = null;
        String fileName = null;
        if (ADMIN_TYPE.equals(type)) {
            loginModel = new LoginModel(ADMIN_TYPE_USER_NAME, ADMIN_TYPE_PASSWORD, ADMIN_TYPE);
            fileName = LOGIN_ADMIN_TYPE_FILE_SAVED_NAME;
        } else if (USER_TYPE.equals(type)) {
            loginModel = new LoginModel(USER_TYPE_USER_NAME, USER_TYPE_PASSWORD, USER_TYPE);
            fileName = LOGIN_USER_TYPE_FILE_SAVED_NAME;
        }
        loginModel.save(fileName);
        return loginModel;
    }

    public static LoginModel getFactoryTypeLoginModel(String type) {
        String fileName = null;
        if (ADMIN_TYPE.equals(type)) {
            fileName = LOGIN_ADMIN_TYPE_FILE_SAVED_NAME;
        } else if (USER_TYPE.equals(type)) {
            fileName = LOGIN_USER_TYPE_FILE_SAVED_NAME;
        }
        return LoginModel.getObjectByName(fileName);
    }

    public boolean checkUserNameAndPassword(LoginModel loginModel) {
        if (loginModel == null) {
            return false;
        }

        boolean isUserNameMatch = userName.equals(loginModel.getUserName());
        boolean isPasswordMatch = password.equals(loginModel.getPassword());

        return isUserNameMatch && isPasswordMatch;
    }

    public void save(String fileName) {
        getLoginModelDao().save(this, fileName);
    }

    public void delete(String fileName) {
        getLoginModelDao().delete(fileName);
    }

    public static LoginModel getObjectByName(String fileName) {
        return getLoginModelDao().getObjectByName(fileName);
    }

    public static LoginModel getObjectByNameFromResource(String fileName) {
        return getLoginModelDao().getObjectByNameFromResource(fileName);
    }

    private static ILoginModelDao getLoginModelDao() {
        return DaoFactory.getDatabase().getLoginModelDao();
    }
}
