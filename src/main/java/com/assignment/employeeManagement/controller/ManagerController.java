package com.assignment.employeeManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.service.ManagerService;

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
	
	
}
