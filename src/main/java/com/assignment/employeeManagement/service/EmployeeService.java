package com.assignment.employeeManagement.service;

import java.util.List;
import java.util.Optional;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.entity.Employee;


public interface EmployeeService {
	Employee addEmployee(EmployeeDTO employeeDTO);
    Employee updateEmployee(Long employeeId, EmployeeDTO employeeDTO);
    void deleteEmployee(Long employeeId);
    Optional<Employee> getEmployeeById(Long employeeId);
    List<Employee> getAllEmployees();
}
