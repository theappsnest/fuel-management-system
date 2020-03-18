package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.Vehicle;
import com.godavari.appsnest.fms.dao.model.report.ReportVehiclesModel;
import com.godavari.appsnest.fms.dao.model.report.VehicleAllModel;
import com.godavari.appsnest.fms.report.utility.FontUtility;
import com.godavari.appsnest.fms.report.utility.ResourceString;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.List;

@Log4j
public class GenerateReportVehicles extends BaseReport {

    private ReportVehiclesModel reportVehiclesModel;

    public GenerateReportVehicles(File fileToBeSaved, ReportVehiclesModel reportVehiclesModel) {
        super(fileToBeSaved);
        this.reportVehiclesModel = reportVehiclesModel;
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
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("current_reading"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("mileage_km_per_hour"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("department"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("hod"));
        rowSubHeaderMap.put(startColumn++, resourceBundle.getString("owner"));
    }

    @Override
    protected void writeMainContent() {

        List<VehicleAllModel> vehicleAllModelList = reportVehiclesModel.getVehicleAllModelList();

        int rowNo = ROW_SUB_HEADER_ROW_NO;

        CellStyle tableMainHeaderStyle = FontUtility.getTableMainHeaderStyle(workbook);
        CellStyle cellValueStyle = FontUtility.getCellValueStyle(workbook);
        CellStyle cellBoldValueStyle = FontUtility.getCellBoldValueStyle(workbook, null);
        CellStyle cellBoldValueStyleLeft = FontUtility.getCellBoldValueStyle(workbook, HorizontalAlignment.LEFT);

        for (int i = 0; i < vehicleAllModelList.size(); i++) {
            VehicleAllModel vehicleAllModel = vehicleAllModelList.get(i);
            Vehicle vehicle = vehicleAllModel.getVehicle();

            Row row = sheet.createRow(rowNo++);
            Cell cell = row.createCell(ROW_SUB_HEADER_COLUMN_NO);
            cell.setCellValue(resourceBundle.getString("vehicle_no") + " " + vehicle.getVehicleNo()
                    + " " + resourceBundle.getString("vehicle_type") + " " + vehicle.getVehicleType().getType());
            cell.setCellStyle(tableMainHeaderStyle);

            sheet.addMergedRegion(FontUtility.getCellRangeAddress(sheet, row.getRowNum(), row.getRowNum(), ROW_HEADER_COL_NO, rowSubHeaderMap.size()));

            if (!vehicleAllModel.getDepartmentVehicleRowModelList().isEmpty()) {
                writeRowSubHeader(rowNo++);
                for (int j = 0; j < vehicleAllModel.getDepartmentVehicleRowModelList().size(); j++) {
                    row = sheet.createRow(rowNo++);

                    Account account = vehicleAllModel.getDepartmentVehicleRowModelList().get(j).getAccount();
                    createContentCell(row, "sr_no", null, j + 1, cellValueStyle);
                    createContentCell(row, "date", null, account.getDateTime().toLocalDate(), cellValueStyle);
                    createContentCell(row, "time", null, account.getDateTime().toLocalTime(), cellValueStyle);
                    createContentCell(row, "output", null, account.getOutput(), cellValueStyle);
                    createContentCell(row, "current_reading", null, account.getCurrentReading(), cellValueStyle);
                    createContentCell(row, "mileage_km_per_hour", null, account.getMileageKmPerHour(), cellValueStyle);
                    createContentCell(row, "department", null, account.getHodManage().getDepartment().getName(), cellValueStyle);
                    createContentCell(row, "hod", null, account.getHodManage().getHeadOfDepartment().getName(), cellValueStyle);
                    createContentCell(row, "owner", null, account.getOwner(), cellValueStyle);
                }
                row = sheet.createRow(rowNo++);
                createContentCell(row, "output", null, vehicleAllModel.getDepartmentVehicleRowModelList().get(0).getTotal(), cellBoldValueStyle);

                Cell hodSignCell = row.createCell(getRowSubHeaderKeyForValue(ResourceString.getString("hod")));
                hodSignCell.setCellValue(resourceBundle.getString("hod_sign")+" : ");
                hodSignCell.setCellStyle(cellBoldValueStyleLeft);
                sheet.addMergedRegion(FontUtility.getCellRangeAddress(sheet, row.getRowNum(), row.getRowNum(), hodSignCell.getColumnIndex(), rowSubHeaderMap.size()));

                rowNo++;
            }
            rowNo++;
        }
    }
}
