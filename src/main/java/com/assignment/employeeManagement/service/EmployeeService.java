package com.assignment.employeeManagement.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.User;


public interface EmployeeService {
	/*
	 * Employee addEmployee(EmployeeDTO employeeDTO); Employee updateEmployee(Long
	 * employeeId, EmployeeDTO employeeDTO); void deleteEmployee(Long employeeId);
	 * Employee getEmployeeById(Long employeeId);
	 */
    List<Employee> getAllEmployees();
    Employee updateSkills(Principal principal, List<String> skills);
    Employee getEmployeeInfo(Principal pincipal);
    User changePassword(Principal principal, String password);
    List<Manager> getAllManagers();
}
