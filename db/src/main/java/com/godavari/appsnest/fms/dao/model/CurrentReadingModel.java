package com.godavari.appsnest.fms.dao.model;

import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;

@Log4j
@Setter
@Getter
public class CurrentReadingModel {

    public static float getCurrentStockReading() throws SQLException {
        return DaoFactory.getDatabase().getReportFromToDao().getCurrentStockReading();
    }
}
