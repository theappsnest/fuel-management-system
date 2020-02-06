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
public class ReportMaster1Model extends BaseReportModel {

    private List<Master1Model> master1ModelList;

    public List<Master1Model> getRowByFromToDate() throws SQLException {
        master1ModelList = DbSqlite.getDatabase().getReportMaster1Dao().getRowByFromToDate(this);
        return master1ModelList;
    }

    @Override
    public boolean isAnyDataAvailableToGenerateReport() {
        if (master1ModelList == null || master1ModelList.isEmpty()) {
            return false;
        }
        return true;
    }
}
