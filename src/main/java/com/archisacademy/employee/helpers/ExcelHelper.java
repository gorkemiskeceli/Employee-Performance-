package com.archisacademy.employee.helpers;

import com.archisacademy.employee.dto.request.EmployeeImportRequest;
import com.archisacademy.employee.dto.response.EmployeeExportDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static List<EmployeeImportRequest> excelToEmployees(InputStream is) {
        try {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<EmployeeImportRequest> employees = new ArrayList<>();
            boolean firstRow = true;

            while (rows.hasNext()) {
                Row row = rows.next();
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                EmployeeImportRequest employee = new EmployeeImportRequest();
                employee.setEmployeeId(getNumericCellValue(row.getCell(0)));
                employee.setFirstName(getStringCellValue(row.getCell(1)));
                employee.setLastName(getStringCellValue(row.getCell(2)));
                employee.setEmail(getStringCellValue(row.getCell(3)));
                employee.setPhoneNumber(getStringCellValue(row.getCell(4)));
                employee.setDepartmentId(getNumericCellValue(row.getCell(5)));
                employee.setPosition(getStringCellValue(row.getCell(6)));
                employee.setDateOfJoining(getStringCellValue(row.getCell(7)));

                employees.add(employee);
            }
            workbook.close();
            return employees;
        } catch (Exception e) {
            throw new RuntimeException("Excel dosyası okunurken hata oluştu: " + e.getMessage());
        }
    }

    private static Long getNumericCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return (long) cell.getNumericCellValue();
        } else {
            try {
                return Long.parseLong(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    private static String getStringCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate().toString();
            }
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return cell.getStringCellValue().trim();
    }
    public static void writeEmployeesToExcel(List<EmployeeExportDto> employees, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Employees");


            Row headerRow = sheet.createRow(0);
            String[] columns = {"Employee ID", "First Name", "Last Name", "Email", "Phone Number", "Department", "Position", "Date of Joining"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            int rowNum = 1;
            for (EmployeeExportDto emp : employees) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(emp.getEmployeeId());
                row.createCell(1).setCellValue(emp.getFirstName());
                row.createCell(2).setCellValue(emp.getLastName());
                row.createCell(3).setCellValue(emp.getEmail());
                row.createCell(4).setCellValue(emp.getPhoneNumber());
                row.createCell(5).setCellValue(emp.getDepartmentName());
                row.createCell(6).setCellValue(emp.getPosition());
                row.createCell(7).setCellValue(emp.getDateOfJoining() != null ? emp.getDateOfJoining().toString() : "");
            }
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            System.out.println("Çalışan verileri başarıyla Excel dosyasına kaydedildi: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası yazılırken hata oluştu: " + e.getMessage());
        }
    }


}
