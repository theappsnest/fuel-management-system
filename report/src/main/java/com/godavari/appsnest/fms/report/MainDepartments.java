package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.dao.model.Department;
import com.godavari.appsnest.fms.dao.model.report.DepartmentAllModel;
import com.godavari.appsnest.fms.dao.model.report.DepartmentVehicleRowModel;
import com.godavari.appsnest.fms.dao.model.report.ReportDepartmentsModel;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MainDepartments {

    public MainDepartments(ReportDepartmentsModel reportDepartmentsModel) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet();
        XSSFRow row;
        XSSFCell cell;

        int rowNo = 0;
        int colNo = 0;
        List<DepartmentAllModel> departmentAllModelList = reportDepartmentsModel.getDepartmentAllModels();
        for (DepartmentAllModel departmentAllModel : departmentAllModelList) {
            Department department = departmentAllModel.getDepartment();

            colNo = 0;
            row = xssfSheet.createRow(rowNo++);

            row.createCell(colNo++).setCellValue("Department: "+department.getName());

            if (!departmentAllModel.getDepartmentVehicleRowModelList().isEmpty()) {
                for (int i = 0; i < departmentAllModel.getDepartmentVehicleRowModelList().size(); i++) {
                    row = xssfSheet.createRow(rowNo++);
                    colNo = 0;

                    DepartmentVehicleRowModel departmentVehicleRowModel = departmentAllModel.getDepartmentVehicleRowModelList().get(i);
                    row.createCell(colNo++).setCellValue(i + 1);
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getDateTime().toLocalDate().toString());
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getDateTime().toLocalTime().toString());
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getOutput());
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getHodManage().getHeadOfDepartment().getName());
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getOwner());
                }
                row = xssfSheet.createRow(rowNo++);
                colNo = 4;
                row.createCell(colNo++).setCellValue(departmentAllModel.getDepartmentVehicleRowModelList().get(0).getTotal());
                xssfSheet.createRow(rowNo++);
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream("Report Departments " + LocalDateTime.now() + " .xlsx");
            xssfWorkbook.write(outputStream);
            xssfWorkbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
