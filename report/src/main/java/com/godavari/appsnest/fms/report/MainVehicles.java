package com.godavari.appsnest.fms.report;

import com.godavari.appsnest.fms.dao.model.Vehicle;
import com.godavari.appsnest.fms.dao.model.report.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MainVehicles {

    public MainVehicles(ReportVehiclesModel reportVehiclesModel) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet();
        XSSFRow row;
        XSSFCell cell;

        int rowNo = 0;
        int colNo = 0;
        List<VehicleAllModel> vehicleAllModels = reportVehiclesModel.getVehicleAllModelList();
        for (VehicleAllModel vehicleAllModel : vehicleAllModels) {
            Vehicle vehicle = vehicleAllModel.getVehicle();

            colNo = 0;
            row = xssfSheet.createRow(rowNo++);

            row.createCell(colNo++).setCellValue("Vehicle no: " + vehicle.getVehicleNo() + " vehicleType: " + vehicle.getVehicleType().getType());

            if (!vehicleAllModel.getDepartmentVehicleRowModelList().isEmpty()) {
                for (int i = 0; i < vehicleAllModel.getDepartmentVehicleRowModelList().size(); i++) {
                    row = xssfSheet.createRow(rowNo++);
                    colNo = 0;

                    DepartmentVehicleRowModel departmentVehicleRowModel = vehicleAllModel.getDepartmentVehicleRowModelList().get(i);
                    row.createCell(colNo++).setCellValue(i + 1);
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getDateTime().toLocalDate().toString());
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getDateTime().toLocalTime().toString());
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getHodManage().getDepartment().getName());
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getHodManage().getHeadOfDepartment().getName());
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getOutput());
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getCurrentReading());
                    row.createCell(colNo++).setCellValue(departmentVehicleRowModel.getAccount().getOwner());
                }
                row = xssfSheet.createRow(rowNo++);
                colNo = 5;
                row.createCell(colNo++).setCellValue(vehicleAllModel.getDepartmentVehicleRowModelList().get(0).getTotal());
                xssfSheet.createRow(rowNo++);
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream("Report Vehicle " + LocalDateTime.now() + " .xlsx");
            xssfWorkbook.write(outputStream);
            xssfWorkbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //new Main()
    }
}
