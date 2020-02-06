package com.godavari.appsnest.fms.dao.concrete;

import com.godavari.appsnest.fms.dao.abstracts.DesktopGenericDaoImpl;
import com.godavari.appsnest.fms.dao.interfaces.IMD5ModelDao;
import com.godavari.appsnest.fms.dao.model.MD5Model;
import com.godavari.appsnest.fms.dao.utility.UtilityMethod;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.*;

import static com.godavari.appsnest.fms.dao.utility.UtilityMethod.JSON_EXTENSION_STRING;

public class MD5ModelDaoImpl extends DesktopGenericDaoImpl<MD5Model> implements IMD5ModelDao {

    private static final String LOG_TAG = MD5ModelDaoImpl.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(LOG_TAG);

    public MD5ModelDaoImpl(String parentFolderName, String saveFileFolder) {
        super(parentFolderName, saveFileFolder, UtilityMethod.createFolderName(parentFolderName, saveFileFolder));
    }


    @Override
    public MD5Model getObjectByName(String fileName) {
        logger.info("getObjectByName, fileName: "+fileName);
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(UtilityMethod.getFileForSaveFolder(filePath, fileName, JSON_EXTENSION_STRING));
            MD5Model md5Model = gson.fromJson(reader, MD5Model.class);
            reader.close();
            return md5Model;
        } catch (Exception e) {
            logger.error("file couldnt be read from filePath: " + filePath + " , fileName: " + fileName, e);
        }
        return null;
    }

    @Override
    public MD5Model getObjectByNameFromResource(String fileName) {
        logger.info("getObjectByNameFromResource, fileName: "+fileName);
        try {
            Gson gson = new Gson();
            InputStream inputStream = MD5ModelDaoImpl.class.getClassLoader().getResourceAsStream(parentFileFolder + "/" + saveFileFolder + "/" + fileName + JSON_EXTENSION_STRING);
            Reader reader = new BufferedReader(new InputStreamReader(inputStream));
            MD5Model md5Model = gson.fromJson(reader, MD5Model.class);
            reader.close();
            return md5Model;
        } catch (Exception e) {
            logger.error("file couldnt be read from filePath: " + parentFileFolder + "/" + saveFileFolder + " , fileName: " + fileName, e);
        }
        return null;
    }
}
