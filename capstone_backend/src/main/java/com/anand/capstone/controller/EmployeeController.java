package com.anand.capstone.controller;

import com.anand.capstone.entity.Credentials;
import com.anand.capstone.entity.Employee;
import com.anand.capstone.helper.EmployeeCsvExporter;
import com.anand.capstone.helper.EmployeeCsvParser;
import com.anand.capstone.service.EmployeeService;
import com.anand.capstone.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeCsvParser employeeCsvParser;

    @Autowired
    private EmployeeCsvExporter employeeCsvExporter;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/getCreds")
    public List<Credentials> getAllCredentials() {
        return employeeService.getAllCredentials();
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

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = passwordEncoder.encode(loginRequest.get("password"));

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Email and password are required.");
        }

        Credentials cred = employeeService.getCredentialsByEmail(email);
        System.out.println("fetched credientioals"+cred.getPassword());
        System.out.println("posted credentials"+password);
        if (cred == null || !passwordEncoder.matches(password, cred.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }

        String token = jwtUtils.generateJwtToken(email);

        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam(required = false) String firstName,
                                                          @RequestParam(required = false) String lastName,
                                                          @RequestParam(required = false) String email,
                                                          @RequestParam(required = false) String designation) {
        List<Employee> searchedEmployees = employeeService.searchEmployees(firstName, lastName, email, designation);
        return ResponseEntity.ok().body(searchedEmployees);
    }

    @DeleteMapping("/delCred")
    public ResponseEntity<String> deleteCredential(@RequestBody Map<String, String> requestBody) {
        try {
            String email = requestBody.get("email");
            if (email == null) {
                return ResponseEntity.badRequest().body("Email is required.");
            }
            employeeService.deleteCredential(email);
            return ResponseEntity.ok().body("Credential deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete credential.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerEmployee(@RequestBody Map<String, String> employeeMap) {
        try {

            String email = employeeMap.get("email");
            String password = employeeMap.get("password");

            employeeMap.remove("password");

            ObjectMapper objectMapper = new ObjectMapper();

            Employee employee = objectMapper.convertValue(employeeMap, Employee.class);

            Credentials cred = new Credentials();
            cred.setPassword(password);
            cred.setEmail(email);

            employeeService.registerEmployee(employee);

            employeeService.registerCred(cred);

            return ResponseEntity.ok().body("Employee registered successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register employee.");
        }
    }
}
