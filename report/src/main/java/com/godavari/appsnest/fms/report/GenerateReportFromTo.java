package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.report.ReportFromToModel;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.List;

@Log4j
public class GenerateReportFromTo extends BaseReport {

    private ReportFromToModel reportFromToModel;

    public GenerateReportFromTo(File fileToBeSaved, ReportFromToModel reportFromToModel) {
        super(fileToBeSaved);
        this.reportFromToModel = reportFromToModel;
        generateExcelReport();
    }

    @Override
    protected void generateExcelReport() {
        addRowSubHeaderReport();
        writeRowSubHeader(ROW_SUB_HEADER_ROW_NO);

        writeMainContent();
        for (int i = 0; i < rowSubHeaderMap.size() + 1; i++) {
            sheet.autoSizeColumn(ROW_SUB_HEADER_COLUMN_NO + i);
        }

        saveAndCloseWorkbook();
    }

    @Override
    protected void addRowSubHeaderReport() {
        int startColumn = ROW_SUB_HEADER_COLUMN_NO;
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("sr_no"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("date"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("input"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("output"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("vehicle_no"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("current_reading"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("mileage_km_per_hour"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("department"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("hod"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("time"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("owner"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("vehicle_type"));
    }

    @Override
    protected void writeMainContent() {
        List<Account> accountList = reportFromToModel.getAccountList();

        for (int i = 0; i < accountList.size(); i++) {
            Row row = sheet.createRow(CONTENT_START_ROW_NO + i);

            Account account = accountList.get(i);

            createContentCell(row, "sr_no", null, i + 1);
            createContentCell(row, "date", null, account.getDateTime().toLocalDate());
            createContentCell(row, "input", null, account.getInput());
            createContentCell(row, "output", null, account.getOutput());
            if (account.getVehicleAssigned() != null) {
                createContentCell(row, "vehicle_no", null, account.getVehicleAssigned().getVehicle().getVehicleNo());
                createContentCell(row, "vehicle_type", null, account.getVehicleAssigned().getVehicle().getVehicleType().getType());
            }

            createContentCell(row, "current_reading", null, account.getCurrentReading());
            createContentCell(row, "mileage_km_per_hour", "", account.getMileageKmPerHour());
            createContentCell(row, "department", null, account.getHodManage().getDepartment().getName());
            createContentCell(row, "hod", null, account.getHodManage().getHeadOfDepartment().getName());
            createContentCell(row, "time", null, account.getDateTime().toLocalTime());
            createContentCell(row, "owner", null, account.getOwner());
        }
    }
}
