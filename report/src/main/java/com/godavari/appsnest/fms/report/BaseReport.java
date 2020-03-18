package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.report.utility.FontUtility;
import com.godavari.appsnest.fms.report.utility.ResourceString;
import com.godavari.appsnest.fms.report.utility.Utility;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Log4j
public abstract class BaseReport {
    public static final int ROW_HEADER_ROW_NO = 1;
    public static final int ROW_HEADER_COL_NO = 1;

    public static final int ROW_SUB_HEADER_ROW_NO = 2;
    public static final int ROW_SUB_HEADER_COLUMN_NO = 1;

    public static final int CONTENT_START_ROW_NO = 3;
    public static final int CONTENT_START_COLUMN_NO = 1;

    protected File fileToBeSaved;

    protected XSSFWorkbook workbook;
    protected XSSFSheet sheet;

    protected Map<Integer, String> rowSubHeaderMap = new HashMap<>();

    protected ResourceBundle resourceBundle;

    public BaseReport(File fileToBeSaved) {
        this.fileToBeSaved = fileToBeSaved;
        this.resourceBundle = ResourceString.getResourceBundle();
        openWorkbook();
    }

    protected void openWorkbook() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
    }

    protected void saveAndCloseWorkbook() {
        try {
            FileOutputStream outputStream = new FileOutputStream(fileToBeSaved);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
    }

    protected abstract void generateExcelReport();

    protected abstract void addRowSubHeaderReport();

    protected abstract void writeMainContent();

    protected void writeRowSubHeader(int rowNo) {

        Row row = sheet.createRow(rowNo);

        CellStyle cellStyle = FontUtility.getTableSubHeaderStyle(workbook);

        Iterator rowSubHeaderMapIterator = rowSubHeaderMap.entrySet().iterator();

        while (rowSubHeaderMapIterator.hasNext()) {
            Map.Entry<Integer, String> mapElement = (Map.Entry) rowSubHeaderMapIterator.next();

            Cell cell = row.createCell(mapElement.getKey());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(mapElement.getValue());
            cell.getCellStyle().setWrapText(true);
        }
    }

    protected Cell createContentCell(Row row, String resourceKey, String columnName, Object value, CellStyle cellStyle) {

        String rowSubHeaderValue = null;
        if (!StringUtils.isEmpty(resourceKey)) {
            rowSubHeaderValue = resourceBundle.getString(resourceKey);
        } else {
            rowSubHeaderValue = columnName;
        }

        Cell cell = row.createCell(getRowSubHeaderKeyForValue(rowSubHeaderValue));
        cell.setCellStyle(cellStyle);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue(Utility.dateFormatter((LocalDate) value));
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
        } else if (value instanceof LocalTime) {
            cell.setCellValue(((LocalTime) value).toString());
        }

        return cell;
    }


    protected int getRowSubHeaderKeyForValue(String value) {
        for (Map.Entry<Integer, String> entry : rowSubHeaderMap.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return -1;
    }

}
