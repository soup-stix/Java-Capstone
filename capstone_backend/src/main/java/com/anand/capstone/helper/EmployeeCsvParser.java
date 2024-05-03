package com.anand.capstone.helper;

import com.anand.capstone.entity.Employee;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Component
public class EmployeeCsvParser {
    public List<Employee> parseCsv(InputStream inputStream) throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                // Assuming CSV format: firstName,lastName,email,dateOfJoining,grade
                Employee employee = new Employee();
                employee.setFirstName(line[0]);
                employee.setLastName(line[1]);
                employee.setEmail(line[2]);
                // Parse dateOfJoining from String to java.sql.Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateOfJoining = new Date(dateFormat.parse(line[3]).getTime());
                employee.setDateOfJoining(dateOfJoining);
                // Set grade
                employee.setGrade(line[4]);
                employees.add(employee);
            }
        } catch (ParseException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }
}