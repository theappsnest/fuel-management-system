package com.godavari.appsnest.fms.dao.model.report;

import com.godavari.appsnest.fms.dao.daofactory.DbSqlite;
import com.godavari.appsnest.fms.dao.model.Account;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.sql.SQLException;
import java.util.List;

@Getter
@Setter
@Log4j
public class ReportFromToModel extends BaseReportModel {

    private List<Account> accountList;

    public List<Account> getRowByFromToDate() throws SQLException {
        accountList = DbSqlite.getDatabase().getReportFromToDao().getRowByFromToDate(this);
        return accountList;
    }

    @Override
    public boolean isAnyDataAvailableToGenerateReport() {
        if (accountList == null || accountList.isEmpty()) {
            return false;
        }
        return true;
    }
}
