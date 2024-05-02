package com.anand.capstone.helper;

import com.anand.capstone.entity.Employee;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EmployeeCsvExporter {
    public void exportEmployeesToCsv(List<Employee> employees, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"employees.csv\"");

        try (CSVWriter writer = new CSVWriter(response.getWriter())) {
            // Define the header row
            String[] header = {"ID", "First Name", "Last Name", "Date of Joining", "Email"};
            writer.writeNext(header);

            // Write data rows
            for (Employee employee : employees) {
                String[] data = {String.valueOf(employee.getId()), employee.getFirstName(), employee.getLastName(),
                        String.valueOf(employee.getDateOfJoining()), employee.getEmail()};
                writer.writeNext(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
