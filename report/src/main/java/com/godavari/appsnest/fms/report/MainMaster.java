package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.report.MasterModel;
import com.godavari.appsnest.fms.dao.model.report.ReportFromToModel;
import com.godavari.appsnest.fms.dao.model.report.ReportMasterModel;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainMaster {

    public MainMaster(ReportMasterModel reportMasterModel)
    {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet();
        XSSFRow row;
        XSSFCell cell;

        int rowNo=0;
        int colNo=0;
        List<MasterModel> masterModelList = reportMasterModel.getMasterModelList();
        for (MasterModel masterModel:masterModelList)
        {
            colNo =0;
            row = xssfSheet.createRow(rowNo++);
            row.createCell(colNo++).setCellValue(rowNo);
            row.createCell(colNo++).setCellValue(masterModel.getMonthYear());
            row.createCell(colNo++).setCellValue(masterModel.getOpeningStock());
            row.createCell(colNo++).setCellValue(masterModel.getInput());
            row.createCell(colNo++).setCellValue(masterModel.getOutput());
            row.createCell(colNo++).setCellValue(masterModel.getClosingStock());
        }

        try {
            FileOutputStream outputStream = new FileOutputStream("Report Master "+System.currentTimeMillis()+" .xlsx");
            xssfWorkbook.write(outputStream);
            xssfWorkbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        //new Main()
    }
}
