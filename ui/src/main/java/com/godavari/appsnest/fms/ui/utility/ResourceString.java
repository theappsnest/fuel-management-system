package com.godavari.appsnest.fms.ui.utility;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceString {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("value/strings", Locale.getDefault(),ResourceString.class.getClassLoader());

    public static String getString(String key)
    {
        return resourceBundle.getString(key);
    }

    public static ResourceBundle getResourceBundle()
    {
        return resourceBundle;
    }
}
