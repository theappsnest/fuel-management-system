package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.dao.model.report.Master1Model;
import com.godavari.appsnest.fms.dao.model.report.ReportMaster1Model;
import com.godavari.appsnest.fms.dao.utility.DatabaseConstant;
import com.godavari.appsnest.fms.report.utility.Utility;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.List;

@Log4j
public class GenerateReportMaster1 extends BaseReport {

    private ReportMaster1Model reportMaster1Model;

    public GenerateReportMaster1(File fileToBeSaved, ReportMaster1Model reportMaster1Model) {
        super(fileToBeSaved);
        this.reportMaster1Model = reportMaster1Model;
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
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("department"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("vehicle_no"));

        int diffMonth = DatabaseConstant.differenceBetweenDates(reportMaster1Model.getFromLocalDate(), reportMaster1Model.getToLocalDate());
        for (int i = 0; i <= diffMonth; i++) {
            rowSubHeaderMap.put(startColumn++, Utility.getMonthYear(reportMaster1Model.getFromLocalDate().plusMonths(i)));
        }
    }

    @Override
    protected void writeMainContent() {
        List<Master1Model> master1ModelList = reportMaster1Model.getMaster1ModelList();

        for (int i = 0; i < master1ModelList.size(); i++) {
            Row row = sheet.createRow(CONTENT_START_ROW_NO + i);

            Master1Model master1Model = master1ModelList.get(i);
            createContentCell(row, "sr_no", null, i + 1);
            createContentCell(row, "department", null, master1Model.getDepartmentName());
            createContentCell(row, "vehicle_no", null, master1Model.getVehicleNo());

            List<Double> monthlyConsumption = master1Model.getMonthlyConsumption();

            for (int j = 0; j < monthlyConsumption.size(); j++) {
                createContentCell(row, null,
                        Utility.getMonthYear(reportMaster1Model.getFromLocalDate().plusMonths(j)),
                        monthlyConsumption.get(j));
            }
        }
    }
}
