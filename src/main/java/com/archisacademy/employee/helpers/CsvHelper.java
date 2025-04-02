package com.archisacademy.employee.helpers;

import com.archisacademy.employee.dto.request.EmployeeImportRequest;
import com.archisacademy.employee.dto.response.EmployeeExportDto;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.*;
import java.util.List;

public class CsvHelper {
    public static List<EmployeeImportRequest> csvToEmployees(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is))) {
            CsvToBean<EmployeeImportRequest> csvToBean = new CsvToBeanBuilder<EmployeeImportRequest>(fileReader)
                    .withType(EmployeeImportRequest.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            throw new RuntimeException("CSV dosyası okunurken hata oluştu: " + e.getMessage());
        }
    }
    public static void writeEmployeesToCsv(List<EmployeeExportDto> employees, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            String[] header = {"Employee ID", "First Name", "Last Name", "Email", "Phone Number", "Department", "Position", "Date of Joining"};
            writer.writeNext(header);

            for (EmployeeExportDto emp : employees) {
                String[] data = {
                        String.valueOf(emp.getEmployeeId()),
                        emp.getFirstName(),
                        emp.getLastName(),
                        emp.getEmail(),
                        emp.getPhoneNumber(),
                        emp.getDepartmentName(),
                        emp.getPosition(),
                        emp.getDateOfJoining() != null ? emp.getDateOfJoining().toString() : ""
                };
                writer.writeNext(data);
            }

            System.out.println("Çalışan verileri başarıyla CSV dosyasına kaydedildi: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("CSV dosyası yazılırken hata oluştu: " + e.getMessage());
        }
    }
}

