package com.anand.capstone.controller;

import com.anand.capstone.entity.Employee;
import com.anand.capstone.helper.EmployeeCsvExporter;
import com.anand.capstone.helper.EmployeeCsvParser;
import com.anand.capstone.service.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeCsvParser employeeCsvParser;

    @Autowired
    private EmployeeCsvExporter employeeCsvExporter;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping("/save")
    public void saveEmployee(@RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employeeService.updateEmployee(id, employee);
    }

    @PostMapping("/uploadCsv")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a CSV file.");
        }

        try {
            List<Employee> employees = employeeCsvParser.parseCsv(file.getInputStream());
            employeeService.saveAllEmployees(employees);
            return ResponseEntity.ok().body("CSV file uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload CSV file.");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam(required = false) String firstName,
                                                          @RequestParam(required = false) String lastName,
                                                          @RequestParam(required = false) String email,
                                                          @RequestParam(required = false) String designation) {
        List<Employee> searchedEmployees = employeeService.searchEmployees(firstName, lastName, email, designation);
        return ResponseEntity.ok().body(searchedEmployees);
    }
}
