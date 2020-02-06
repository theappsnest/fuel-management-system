package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.model.report.DepartmentAllModel;
import com.godavari.appsnest.fms.dao.model.report.DepartmentVehicleRowModel;
import com.godavari.appsnest.fms.dao.model.report.ReportDepartmentsModel;
import com.godavari.appsnest.fms.dao.model.report.ReportFromToModel;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.List;

@Log4j
public class GenerateReportDepartments extends BaseReport {

    private ReportDepartmentsModel reportDepartmentsModel;

    public GenerateReportDepartments(File fileToBeSaved, ReportDepartmentsModel reportDepartmentsModel) {
        super(fileToBeSaved);
        this.reportDepartmentsModel = reportDepartmentsModel;
        generateExcelReport();
    }

    @Override
    protected void generateExcelReport() {
        addRowSubHeaderReport();

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
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("time"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("output"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("hod"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("owner"));
    }

    @Override
    protected void writeMainContent() {

        List<DepartmentAllModel> departmentAllModelList = reportDepartmentsModel.getDepartmentAllModels();

        int rowNo = ROW_SUB_HEADER_ROW_NO;

        for (int i = 0; i < departmentAllModelList.size(); i++) {
            DepartmentAllModel departmentAllModel = departmentAllModelList.get(i);
            Department department = departmentAllModel.getDepartment();

            Row row = sheet.createRow(rowNo++);
            Cell cell = row.createCell(ROW_SUB_HEADER_COLUMN_NO);
            cell.setCellValue(resourceBundle.getString("department") + " " + department.getName());

            if (!departmentAllModel.getDepartmentVehicleRowModelList().isEmpty()) {
                writeRowSubHeader(rowNo++);
                for (int j = 0; j < departmentAllModel.getDepartmentVehicleRowModelList().size(); j++) {
                    row = sheet.createRow(rowNo++);

                    Account account = departmentAllModel.getDepartmentVehicleRowModelList().get(j).getAccount();
                    createContentCell(row, "sr_no", null, j + 1);
                    createContentCell(row, "date", null, account.getDateTime().toLocalDate());
                    createContentCell(row, "time", null, account.getDateTime().toLocalTime());
                    createContentCell(row, "output", null, account.getOutput());
                    createContentCell(row, "hod", null, account.getHodManage().getHeadOfDepartment().getName());
                    createContentCell(row, "owner", null, account.getOwner());
                }
                row = sheet.createRow(rowNo++);
                createContentCell(row, "output", null, departmentAllModel.getDepartmentVehicleRowModelList().get(0).getTotal());
                rowNo++;
            }
        }
    }
}
