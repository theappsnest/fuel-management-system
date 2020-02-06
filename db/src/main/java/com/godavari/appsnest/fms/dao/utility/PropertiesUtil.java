package com.godavari.appsnest.fms.dao.utility;

import lombok.extern.log4j.Log4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j
public class PropertiesUtil {

    private static PropertiesUtil instance;
    private Map<String, String> cachedProperty;

    private PropertiesUtil() {
        try {
            this.cachedProperty = new HashMap<>();
        } catch (Exception e) {
            log.error(e);
        }
    }

    public String getPropertyValue(String key) {
        for (Map.Entry<String, String> entry : cachedProperty.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String savePropertyValue(String key, String value) {
        return cachedProperty.put(key, value);

    }

    public static PropertiesUtil getInstance() {
        if (instance == null) {
            instance = new PropertiesUtil();
        }
        return instance;
    }
}
