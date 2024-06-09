package com.assignment.employeeManagement.service;

import java.util.List;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.dto.ManagerDTO;
import com.assignment.employeeManagement.dto.ProjectDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;

public interface AdminService {
	   Employee addEmployee(EmployeeDTO employeeDTO);
	    Project addProject(ProjectDTO projectDTO);
	    Manager addmanager(ManagerDTO managerDTO);
	    List<Manager> getAllManager();
	    List<Employee> getAllEmployees();
	    List<Project> getAllProjects();
	    List<Request> getAllRequest();
	    Employee assignProjectToEmployee(Long employeeId, Long projectId);
	    Employee unassignProjectFromEmployee(Long employeeId);
	    Request approveRequest(Long requestId);
	    Request rejectRequest(Long requestId);
	    void deleteEmployee(Long employeeId);
	    Employee updateEmployee(Long employeeId, EmployeeDTO employeeDTO);
		Employee getEmployeeById(Long employeeId);
}
