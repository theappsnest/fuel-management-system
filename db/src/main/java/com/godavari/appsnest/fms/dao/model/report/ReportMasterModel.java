package com.godavari.appsnest.fms.dao.model.report;

import com.godavari.appsnest.fms.dao.daofactory.DbSqlite;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;
import java.util.List;

@Getter
@Setter
@Log4j
public class ReportMasterModel extends BaseReportModel {

    private List<MasterModel> masterModelList;

    public List<MasterModel> getRowByFromToDate() throws SQLException {
        masterModelList = DbSqlite.getDatabase().getReportMasterDao().getRowByFromToDate(this);
        return masterModelList;
    }

    @Override
    public boolean isAnyDataAvailableToGenerateReport() {
        if (masterModelList == null || masterModelList.isEmpty()) {
            return false;
        }
        return true;
    }
}
