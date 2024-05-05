package com.anand.capstone.controller;

import com.anand.capstone.entity.Employee;
import com.anand.capstone.helper.EmployeeCsvExporter;
import com.anand.capstone.helper.EmployeeCsvParser;
import com.anand.capstone.service.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.anand.capstone.entity.Employee;
import com.anand.capstone.repository.EmployeeRepository;


@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeCsvParser employeeCsvParser;

    @Autowired
    private EmployeeCsvExporter employeeCsvExporter;

    private Employee employee;

    @Autowired
    private EmployeeRepository employeeRepository;

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

    @PostMapping("/Login")
    public String login(@RequestBody Employee employee) {
        String email = employee.getEmail();
        String password = employee.getPassword();
        Employee emp = employeeRepository.findByEmail(email);
        if (emp != null) {
            if (emp.getPassword().equals(password)) {
                return "Login Successful";
            } else {
                return "Invalid Password";
            }
        } else {
            return "Invalid Email";
        }
    }


    @PostMapping(value = "/uploadCsv", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> uploadCsv(@RequestBody String csvData) {
        // Parse the CSV data and save employees
        try {
            List<Employee> employees = employeeCsvParser.parseCsv(new ByteArrayInputStream(csvData.getBytes()));
            employeeService.saveAllEmployees(employees);
            return ResponseEntity.ok().body("CSV data uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload CSV data.");
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
