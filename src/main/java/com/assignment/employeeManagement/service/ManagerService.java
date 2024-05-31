package com.assignment.employeeManagement.service;

import java.security.Principal;
import java.util.List;

import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;

public interface ManagerService {
	List<Employee> getAllEmployees();
    List<Manager> getAllManagers();
    List<Project> getAllProjects();
    List<Employee> getAllEmployeeByProject(Long projectId);
    List<Employee> filterEmployeesBySkills(List<String> skills);
    List<Employee> getUnassignedEmployees();
    Request requestEmployeesForProject(String email, Long projectId, List<Long> employeeIds);
	Manager getManagerInfo(Principal principal);
}
