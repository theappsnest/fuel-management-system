package com.godavari.appsnest.fms.dao.abstracts;

import com.godavari.appsnest.fms.dao.interfaces.IDesktopGenericDao;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import com.godavari.appsnest.fms.dao.utility.UtilityMethod;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static com.godavari.appsnest.fms.dao.utility.UtilityMethod.JSON_EXTENSION_STRING;

public abstract class DesktopGenericDaoImpl<T> implements IDesktopGenericDao<T> {

    private static final String LOG_TAG = GenericDaoImpl.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(LOG_TAG);

    protected String parentFileFolder;
    protected String saveFileFolder;
    protected String filePath;

    public DesktopGenericDaoImpl(String parentFileFolder, String saveFileFolder, String filePath) {
        this.parentFileFolder = parentFileFolder;
        this.saveFileFolder = saveFileFolder;
        this.filePath = filePath;
    }

    @Override
    public void save(T object, String fileName) {
        logger.info("save, fileName"+fileName);
        try {
            File file = UtilityMethod.getFileForSaveFolder(filePath, fileName, JSON_EXTENSION_STRING);
            if (!file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }

            Gson gson = new Gson();
            Writer writer = new FileWriter(file, false);
            gson.toJson(object, writer);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            logger.error("file couldnt be saved: fileName: " + fileName, e);
        }
    }

    @Override
    public void delete(String fileName) {
        logger.info("delete fileName: "+fileName);
        try {
            File file = (UtilityMethod.getFileForSaveFolder(filePath, fileName, JSON_EXTENSION_STRING));
            file.delete();
        } catch (Exception e) {

        }
    }

    @Override
    public void update(T object){
        DatabaseConstant.getThrowsFunctionNotSupported("DesktopGenericDaoImpl", "update");
    }

    @Override
    public List<String> getAllSavedByNameList() {
        logger.info("getAllSavedByNameList");
        List<String> savedNameList = new ArrayList<>();
        File file = UtilityMethod.getFileForSaveFolder(filePath, null, null);
        if (file.isDirectory()) {
            for (File localFile : file.listFiles(UtilityMethod.getFileFilter(".json"))) {
                savedNameList.add(localFile.getName().substring(0, localFile.getName().indexOf(".")));
            }
        }
        return savedNameList;
    }
}
