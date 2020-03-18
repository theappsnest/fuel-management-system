package com.godavari.appsnest.fms.dao.utility;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileFilter;

public class UtilityMethod {

    public static final String JSON_EXTENSION_STRING = ".json";
    public static final String PDF_EXTENSION_STRING = ".pdf";
    public static final String XLSX_EXTENSION_STRING = ".xlsx";

    public static final String FILE_NAME_INVALID_SYMBOL_REGREX = "[*.\\\\\"/:;|,]";

    public static String createFolderName(String parentFolderName, String folderName) {
        return parentFolderName + File.separator + folderName;
    }

    public static File getFileForSaveFolder(@Nonnull String folderName, @Nullable String fileName, String extension) {
        // here not requied to remove spedial symbol from the folder name, since folder are created by us only
        String path = folderName;
        if (!StringUtils.isEmpty(fileName)) {
            path += File.separator + removeSpecialSymbol(fileName) + extension;
        }
        return new File(path);
    }

    public static String removeSpecialSymbol(String value) {
        return value.replaceAll(FILE_NAME_INVALID_SYMBOL_REGREX, "");
    }

    public static FileFilter getFileFilter(String fileExtension) {
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(fileExtension)) {
                    return true;
                }
                return false;
            }
        };

        return fileFilter;
    }

    public static String formatStringUpperCase(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        return text.toUpperCase().trim();
    }

    public static String formatString(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        return StringUtils.capitalize(text.trim());
    }

}
