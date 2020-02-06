package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.dao.model.Account;
import com.godavari.appsnest.fms.dao.model.report.ReportFromToModel;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Main {

    public Main(ReportFromToModel reportFromToModel)
    {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet();
        XSSFRow row;
        XSSFCell cell;

        int rowNo=0;
        int colNo=0;
        List<Account> accountList = reportFromToModel.getAccountList();
        for (Account account:accountList)
        {
            colNo =0;
            row = xssfSheet.createRow(rowNo++);
            row.createCell(colNo++).setCellValue(account.getId());
            row.createCell(colNo++).setCellValue(account.getDateTime());
            row.createCell(colNo++).setCellValue(account.getHodManage().getId());
            row.createCell(colNo++).setCellValue(account.getVehicleAssigned().getId());
            row.createCell(colNo++).setCellValue(account.getCurrentReading());
            row.createCell(colNo++).setCellValue(account.getInput());
            row.createCell(colNo++).setCellValue(account.getOutput());
            row.createCell(colNo++).setCellValue(account.getOwner());
            row.createCell(colNo++).setCellValue(account.getHodManage().getDepartment().getId());
            row.createCell(colNo++).setCellValue(account.getHodManage().getDepartment().getName());
            row.createCell(colNo++).setCellValue(account.getHodManage().getHeadOfDepartment().getId());
            row.createCell(colNo++).setCellValue(account.getHodManage().getHeadOfDepartment().getName());
            row.createCell(colNo++).setCellValue(account.getVehicleAssigned().getDepartment().getId());
            row.createCell(colNo++).setCellValue(account.getVehicleAssigned().getDepartment().getName());
            row.createCell(colNo++).setCellValue(account.getVehicleAssigned().getVehicle().getId());
            row.createCell(colNo++).setCellValue(account.getVehicleAssigned().getVehicle().getVehicleNo());
            row.createCell(colNo++).setCellValue(account.getVehicleAssigned().getVehicle().getVehicleType().getId());
            row.createCell(colNo++).setCellValue(account.getVehicleAssigned().getVehicle().getVehicleType().getType());
        }

        try {
            FileOutputStream outputStream = new FileOutputStream("firstReport.xlsx");
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
