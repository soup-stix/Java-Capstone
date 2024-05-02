package com.anand.capstone.service;

import com.anand.capstone.entity.Employee;
import com.anand.capstone.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public void saveAllEmployees(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }

    public void updateEmployee(Long id, Employee employee) {
        if (employeeRepository.existsById(id)) {
            employee.setId(id);
            employeeRepository.save(employee);
        }
    }

    public List<Employee> searchEmployees(String firstName, String lastName, String email, String grade) {
        // Prepare search parameters
        firstName = prepareSearchParameter(firstName);
        lastName = prepareSearchParameter(lastName);
        email = prepareSearchParameter(email);
        grade = prepareSearchParameter(grade);

        System.out.println(firstName+lastName+email+grade);

        // Perform wildcard search using LIKE operator in JPQL
        return employeeRepository.findByParameters(firstName, lastName, email, grade);
    }

    private String prepareSearchParameter(String parameter) {
        if (parameter == null || parameter.equals("*")) {
            return "%"; // Match any value
        } else {
            return "%" + parameter + "%"; // Match parameter partially
        }
    }
}


