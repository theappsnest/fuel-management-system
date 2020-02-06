package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.dao.model.report.Master1Model;
import com.godavari.appsnest.fms.dao.model.report.ReportMaster1Model;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainMaster1 {

    public MainMaster1(ReportMaster1Model reportMaster1Model)
    {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet();
        XSSFRow row;
        XSSFCell cell;

        int rowNo=0;
        int colNo=0;
        List<Master1Model> master1ModelList = reportMaster1Model.getMaster1ModelList();
        for (Master1Model master1Model:master1ModelList)
        {
            colNo =0;
            row = xssfSheet.createRow(rowNo++);
            row.createCell(colNo++).setCellValue(rowNo);
            row.createCell(colNo++).setCellValue(master1Model.getDepartmentName());
            row.createCell(colNo++).setCellValue(master1Model.getVehicleNo());
            for (int i=0;i<master1Model.getMonthlyConsumption().size();i++)
            {
                row.createCell(colNo++).setCellValue(master1Model.getMonthlyConsumption().get(i));
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream("Report Master1 "+System.currentTimeMillis()+" .xlsx");
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
