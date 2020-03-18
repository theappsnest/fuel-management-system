package com.godavari.appsnest.fms.report.utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class FontUtility {

    public static final int FONT_SIZE_18 = 18;
    public static final int FONT_SIZE_16_MAIN_HEADING = 16;
    public static final int FONT_SIZE_14_SUB_HEADING = 14;
    public static final int FONT_SIZE_12_TEXT_HEADING = 12;
    public static final int FONT_SIZE_10 = 10;
    public static final int FONT_SIZE_9 = 9;
    public static final int FONT_SIZE_8 = 8;

    public static Font getCourierBold(Workbook wb, int fontSize) {
        Font font = wb.createFont();
        //font.setFontHeightInPoints((short)10);
        font.setFontName("Courier-Bold");
        //font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setFontHeightInPoints((short) fontSize);
        //font.setItalic(false);
        return font;
    }

    public static CellStyle getTableSubHeaderStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(getCourierBold(wb, FONT_SIZE_18));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        return cellStyle;
    }

    public static CellStyle getTableColumnHeaderStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(getCourierBold(wb, FONT_SIZE_18));
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        return cellStyle;
    }

    public static XSSFCellStyle getCellValueStyle(Workbook wb) {
        XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();
        cellStyle.setFont(getCourier(wb, FONT_SIZE_18));
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        return cellStyle;
    }

    public static CellStyle getTableMainHeaderStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(getCourierBold(wb, FONT_SIZE_18));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        return cellStyle;
    }

    public static CellStyle getCellBoldValueStyle(Workbook wb, HorizontalAlignment horizontalAlignment) {
        XSSFCellStyle cellStyle = getCellValueStyle(wb);
        cellStyle.getFont().setBold(true);
        if (horizontalAlignment!=null)
        {
            cellStyle.setAlignment(horizontalAlignment);
        }
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(222, 230, 229)));
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        return cellStyle;
    }

    public static Font getCourier(Workbook wb, int fontSize) {
        Font font = wb.createFont();
        //font.setFontHeightInPoints((short)10);
        font.setFontName("Courier");
        //font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) fontSize);
        font.setBold(false);
        //font.setItalic(false);
        return font;
    }

    public static CellRangeAddress getCellRangeAddress(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress cellAddresses = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, cellAddresses, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, cellAddresses, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, cellAddresses, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, cellAddresses, sheet);
        return cellAddresses;
    }
}
