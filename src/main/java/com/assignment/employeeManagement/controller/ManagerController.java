package com.assignment.employeeManagement.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.service.ManagerService;
import com.assignment.employeeManagement.service.RequestService;

@RestController
@CrossOrigin
@RequestMapping("/api/manager")
public class ManagerController {

	@Autowired
	private ManagerService managerService;
	
	
	@GetMapping("/employee")
	public List<Employee> getAllEmployee(){
		return managerService.getAllEmployees();
	}
	
	@GetMapping("/manager")
	public List<Manager> getAllManager(){
		return managerService.getAllManagers();
	}
	
	//All projects list
	@GetMapping("/project")
	public List<Project> getAllProject(){
		return managerService.getAllProjects();
	}
	
	//Filter unassigned employees
	@GetMapping("/employee/unassigned")
	public List<Employee> getUnassignedEmployees(){
		return managerService.getUnassignedEmployees();
	}
	
	//Filter employee based on skills
	@GetMapping("/employee/filter")
	public List<Employee> filterAllEmployeesBySkills(@RequestParam List<String> skills){
		return managerService.filterEmployeesBySkills(skills);
	}
	
	@PostMapping("/request/employees")
    public ResponseEntity<Request> requestEmployeesForProject(
            @RequestParam String email,
            @RequestParam Long projectId,
            @RequestBody List<Long> employeeIds) {
        
        Request request = managerService.requestEmployeesForProject(email, projectId, employeeIds);
        return ResponseEntity.ok(request);
    }
	
	@GetMapping("/info")
	public Manager getManagerInfo(Principal principal) {
		Manager manager = managerService.getManagerInfo(principal);
		return manager;
	}
	
	@GetMapping("/employee/project/{projectId}")
	public List<Employee> getEmployeeListByProjectId(@PathVariable Long projectId){
		List<Employee> employeeList = managerService.getAllEmployeeByProject(projectId);
		return employeeList;
	}
}
