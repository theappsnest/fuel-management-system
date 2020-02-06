package com.godavari.appsnest.fms.dao.interfaces;

import com.godavari.appsnest.fms.dao.model.LoginModel;

public interface ILoginModelDao extends IDesktopGenericDao<LoginModel> {
    LoginModel getObjectByNameFromResource(String name);
}
