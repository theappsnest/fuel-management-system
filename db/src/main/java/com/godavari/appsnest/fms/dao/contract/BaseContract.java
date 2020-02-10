package com.godavari.appsnest.fms.dao.contract;

import java.util.ArrayList;
import java.util.List;

public class BaseContract {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT ="created_at";
    public static final String COLUMN_UPDATED_AT ="updated_at";

    public static List<String> BASE_COLUMN_NAME_LIST = new ArrayList<String>() {
        {
           add(COLUMN_ID);
           //add(COLUMN_CREATED_AT);
           //add(COLUMN_UPDATED_AT);
        }};
}
