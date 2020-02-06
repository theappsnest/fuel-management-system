package com.godavari.appsnest.fms.dao.interfaces;

import javax.annotation.Nonnull;
import java.util.List;

public interface IDesktopGenericDao<T> {
    void save(T object, String fileName);

    void delete(String fileName);

    void update(T object);

    T getObjectByName(@Nonnull String name);

    List<String> getAllSavedByNameList();
}
