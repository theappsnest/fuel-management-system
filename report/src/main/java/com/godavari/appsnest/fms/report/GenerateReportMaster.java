package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.dao.model.report.MasterModel;
import com.godavari.appsnest.fms.dao.model.report.ReportMasterModel;
import com.godavari.appsnest.fms.report.utility.Utility;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.List;

@Log4j
public class GenerateReportMaster extends BaseReport {

    private ReportMasterModel reportMasterModel;

    public GenerateReportMaster(File fileToBeSaved, ReportMasterModel reportMasterModel) {
        super(fileToBeSaved);
        this.reportMasterModel = reportMasterModel;
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
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("month_year"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("opening_stock"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("inword"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("dispatch"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("closing_stock"));
    }

    @Override
    protected void writeMainContent() {
        List<MasterModel> masterModelList = reportMasterModel.getMasterModelList();

        for (int i = 0; i < masterModelList.size(); i++) {
            Row row = sheet.createRow(CONTENT_START_ROW_NO + i);

            MasterModel masterModel = masterModelList.get(i);

            createContentCell(row, "sr_no", null, i + 1);
            createContentCell(row, "month_year", null, Utility.getMonthYear(masterModel.getMonthYear()));
            createContentCell(row, "opening_stock", null, masterModel.getOpeningStock());
            createContentCell(row, "inword", null, masterModel.getInput());
            createContentCell(row, "dispatch", null,masterModel.getOutput());
            createContentCell(row, "closing_stock", null, masterModel.getClosingStock());
        }
    }
}
