package com.godavari.appsnest.fms.dao;

import com.godavari.appsnest.fms.dao.daofactory.DbSqlite;
import com.godavari.appsnest.fms.dao.interfaces.IAccountDao;
import com.godavari.appsnest.fms.dao.model.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static String[] hodNameArray = {"MHASKE"
            , "VAIKOS"
            , "DEEPAK"
            , "MOHAN EDKE"
            , "RANJAY"
            , "ALOK"
            , "BUJURGE"
            , "SUNIL CHAVAN"
            , "LAKDE"
            , "KK"
            , "NITIN DEOLE"
            , "SAGAR SAWJI"
            , "KAVESH"
            , "AMOL PATIL"
            , "PRATIK"
            , "SAINI"
            , "BHAGWAN MATHLE" };

    static String[] vehicleTypeArray = {
            "MAX"
            , "DZIRE"
            , "MAGIC"
            , "INNOVA"
            , "FORTUNER"
            , "SWIFT"
            , "MAHINDRA MAX"
            , "SCORPIO"
            , "CRANE"
            , "TANKER"
            , "TRUCK"
            , "TRACTOR"
            , "JCB"
            , "Nexon"
            , "Supro"
            , "TRAVELLER"
            , "TRAILER"
            , "TIPPER"
    };

    static List<String> hodManage = new ArrayList<String>() {{
        add("NEW FURNICE,MHASKE");
        add("OLD FURNICE,MHASKE");
        add("ELECTRIC PLANT,D.G VAIKOS");
        add("ROLLING MILL,ALOK");
        add("MAINTENANCE,SAGAR SAWJI");
        add("MACHENICAL,ALOK");
        add("WORKSHOP,RANJAY/KAVISH");
        add("UTILITY,NITIN DEOLE");
        add("CCM,SAINI");
        add("BBM,ALOK");
        add("ROLL ASSEMBLY,RANJAY");
        add("CIVIL,BUJURGE");
        add("PROJECT,MOHAN EDKE");
        add("BYPASS MILL,ALOK");
        add("ELECTRIC ROLLING MILL,DEEPAK");
        add("GUIDE SHOP,AMOL PATIL");
        add("CRUSHER,BHAGWAN MATHLE");
        add("TRACTOR,LAKDE");
        add("LORD VEHICLE,SUNIL CHAVAN");
        add("VEHICLE,KK");
    }};

    static List<String> vehicleAssigned = new ArrayList<String>() {
        {
            add("MH-21-BF-7785,Nexon,VEHICLE");
            add("MH-21-BF-0496,DZIRE,VEHICLE");
            add("MH-21-V-8216,MAGIC,VEHICLE");
            add("MH-21-X-8116,Supro,VEHICLE");
            add("MH-21-AX-3456,INNOVA,VEHICLE");
            add("MH-21-BF-3456,FORTUNER,VEHICLE");
            add("MH-21-AG-7785,INNOVA,VEHICLE");
            add("MH-21-V-5542,SWIFT,VEHICLE");
            add("MH-21-V-7380,SWIFT,VEHICLE");
            add("MH-21-V-3674,MAHINDRA MAX,VEHICLE");
            add("MH-21-AM-2121,SCORPIO,VEHICLE");
            add("MH-21-AN-2121,SCORPIO,VEHICLE");
            add("MH-21-AD-2699,CRANE,LORD VEHICLE");
            add("MH-21-AD-2499,CRANE,LORD VEHICLE");
            add("MH-21-AD-4788,CRANE,LORD VEHICLE");
            add("MH-21-BF-3537,CRANE,LORD VEHICLE");
            add("MH-04-BG-6487,TANKER,LORD VEHICLE");
            add("MH-21-X-5427,TRUCK,LORD VEHICLE");
            add("MH-21-D-2221,CRANE,LORD VEHICLE");
            add("MH-21-BF-0216,JCB,LORD VEHICLE");
            add("MH-21-BF-3368,CRANE,LORD VEHICLE");
            add("MH-21-D-9631,TRUCK,LORD VEHICLE");
            add("MH-20-AT-4559,TRUCK,LORD VEHICLE");
            add("MH-04-CA-7075,TANKER,LORD VEHICLE");
            add("MH-21-X-5316,TRAILER,LORD VEHICLE");
            add("MH-04-DD-5282,TIPPER,LORD VEHICLE");
            add("MH-21-AD-0809,TRACTOR,TRACTOR");
            add("MH-21-D-2405,TRACTOR,TRACTOR");
            add("MH-21-AD-5602,TRACTOR,TRACTOR");
            add("MH-21-AN-3155,TRACTOR,TRACTOR");
        }
    };

    public static void addVehicleAssigned() {
        try {
            String vehicleNo = null;
            String vehicleTypeName = null;
            String departmentName = null;
            for (String row : vehicleAssigned) {
                String[] rowArray = row.split(",");
                vehicleNo = rowArray[0];
                vehicleTypeName = rowArray[1];
                departmentName = rowArray[2];

                Department department =Department.getRowByName(departmentName);
                VehicleType vehicleType= VehicleType.getRowByVehicleType(vehicleTypeName);

                VehicleAssigned vehicleAssigned = new VehicleAssigned();
                vehicleAssigned.setDepartment(department);

                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleNo(vehicleNo);
                vehicle.setVehicleType(vehicleType);

                vehicle.insert();

                vehicleAssigned.setVehicle(vehicle);
                vehicleAssigned.insert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDepartment() {
        try {
            String departmentName = null;
            String hodName = null;
            for (String row : hodManage) {
                System.out.println("row: " + row);
                String[] dataArray = row.split(",");
                departmentName = dataArray[0];
                hodName = dataArray[1];

                HodManage hodManage = new HodManage();

                Department department = new Department();
                department.setName(departmentName);
                department.insert();

                hodManage.setDepartment(department);

                HeadOfDepartment headOfDepartment = HeadOfDepartment.getRowByHodName(hodName);
                if (headOfDepartment != null) {
                    hodManage.setHeadOfDepartment(headOfDepartment);
                    hodManage.insert();
                } else {
                    System.out.println("cant add " + departmentName + " " + hodName);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            //addHod();
            //addDepartment();
            //addVehicleType();
            addVehicleAssigned();

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    private static void addHod() {
        try {


            for (int i = 0; i < hodNameArray.length; i++) {
                HeadOfDepartment headOfDepartment = new HeadOfDepartment();
                headOfDepartment.setName(hodNameArray[i]);
                headOfDepartment.insert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addVehicleType() {
        try {

            for (int i = 0; i < vehicleTypeArray.length; i++) {
                VehicleType vehicleType = new VehicleType();
                vehicleType.setType(vehicleTypeArray[i]);
                vehicleType.insert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
