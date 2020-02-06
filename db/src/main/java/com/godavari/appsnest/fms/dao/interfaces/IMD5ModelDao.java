package com.godavari.appsnest.fms.dao.interfaces;

import com.godavari.appsnest.fms.dao.model.MD5Model;

public interface IMD5ModelDao extends IDesktopGenericDao<MD5Model> {
    MD5Model getObjectByNameFromResource(String name);
}
