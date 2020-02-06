package com.godavari.appsnest.fms.report.utility;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceString {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("value/report_strings", Locale.getDefault());

    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
