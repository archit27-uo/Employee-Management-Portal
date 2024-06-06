package com.assignment.employeeManagement.service;

import java.security.Principal;
import java.util.List;

import com.assignment.employeeManagement.dto.ManagerInfoDTO;
import com.assignment.employeeManagement.dto.RequestDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;

public interface ManagerService {
	List<Employee> getAllEmployees();
    List<Manager> getAllManagers();
    List<Project> getAllProjects();
    List<Request> getAllRequestByManager(Long managerId);
    List<Employee> getAllEmployeeByProject(Long projectId);
    List<Employee> filterEmployeesBySkills(List<String> skills);
    List<Employee> getUnassignedEmployees();
    List<Employee> getAllEmployeeByManager(Principal principal);
    Request requestEmployeesForProject(RequestDTO requestDTO);
	ManagerInfoDTO getManagerInfo(Principal principal);
}
