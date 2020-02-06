package com.godavari.appsnest.fms.dao.concrete;

import com.godavari.appsnest.fms.dao.abstracts.DesktopGenericDaoImpl;
import com.godavari.appsnest.fms.dao.interfaces.ILoginModelDao;
import com.godavari.appsnest.fms.dao.model.LoginModel;
import com.godavari.appsnest.fms.dao.utility.UtilityMethod;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;

import java.io.*;

import static com.godavari.appsnest.fms.dao.utility.UtilityMethod.JSON_EXTENSION_STRING;

@Log4j
public class LoginModelDaoImpl extends DesktopGenericDaoImpl<LoginModel> implements ILoginModelDao {

    public LoginModelDaoImpl(String parentFolderName, String saveFileFolder) {
        super(parentFolderName, saveFileFolder, UtilityMethod.createFolderName(parentFolderName, saveFileFolder));
    }


    @Override
    public LoginModel getObjectByName(String fileName) {
        log.info("getObjectByName, fileName: " + fileName);
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(UtilityMethod.getFileForSaveFolder(filePath, fileName, JSON_EXTENSION_STRING));
            LoginModel loginModel = gson.fromJson(reader, LoginModel.class);
            reader.close();
            return loginModel;
        } catch (Exception e) {
            log.error("file couldnt be read from filePath: " + filePath + " , fileName: " + fileName, e);
        }
        return null;
    }

    @Override
    public LoginModel getObjectByNameFromResource(String fileName) {
        log.info("getObjectByNameFromResource, fileName: " + fileName);
        try {
            Gson gson = new Gson();
            InputStream inputStream = LoginModelDaoImpl.class.getClassLoader().getResourceAsStream(parentFileFolder + "/" + saveFileFolder + "/" + fileName + JSON_EXTENSION_STRING);
            Reader reader = new BufferedReader(new InputStreamReader(inputStream));
            LoginModel loginModel = gson.fromJson(reader, LoginModel.class);
            reader.close();
            return loginModel;
        } catch (Exception e) {
            log.error("file couldnt be read from filePath: " + parentFileFolder + "/" + saveFileFolder + " , fileName: " + fileName, e);
        }
        return null;
    }
}
