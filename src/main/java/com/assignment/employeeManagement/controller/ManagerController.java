package com.assignment.employeeManagement.controller;

import java.security.Principal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.assignment.employeeManagement.dto.ManagerInfoDTO;
import com.assignment.employeeManagement.dto.RequestDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.service.ManagerService;


@RestController
@CrossOrigin
@RequestMapping("/api/manager")
public class ManagerController {

	private static final Logger logger = LogManager.getLogger(EmployeeController.class);
	
	@Autowired
	private ManagerService managerService;
	
	
	@GetMapping("/employee")
	public List<Employee> getAllEmployee(){
		logger.info("API hit: /api/manager/employee method:GET");
		return managerService.getAllEmployees();
	}
	
	@GetMapping("/manager")
	public List<Manager> getAllManager(){
		logger.info("API hit: /api/manager/manager method:GET");
		return managerService.getAllManagers();
	}
	
	//All projects list
	@GetMapping("/project")
	public List<Project> getAllProject(){
		logger.info("API hit: /api/manager/project method:GET");
		return managerService.getAllProjects();
	}
	
	//Filter unassigned employees
	@GetMapping("/employee/unassigned")
	public List<Employee> getUnassignedEmployees(){
		logger.info("API hit: /api/manager/employee/unassigned method:GET");
		return managerService.getUnassignedEmployees();
	}
	
	//Filter employee based on skills
	@GetMapping("/employee/filter")
	public List<Employee> filterAllEmployeesBySkills(@RequestParam List<String> skills){
		logger.info("API hit: /api/manager/employee/filter method:GET");
		return managerService.filterEmployeesBySkills(skills);
	}
	
	@PostMapping("/request/employees")
    public ResponseEntity<Request> requestEmployeesForProject(@RequestBody RequestDTO requestDTO) {
		logger.info("API hit: /api/manager/employee method:POST body: "+requestDTO);
        Request request = managerService.requestEmployeesForProject(requestDTO);
        return ResponseEntity.ok(request);
    }
	
	@GetMapping("/info")
	public ManagerInfoDTO getManagerInfo(Principal principal) {
		logger.info("API hit: /api/manager/info method:GET");
		ManagerInfoDTO manager = managerService.getManagerInfo(principal);
		return manager;
	}
	
	@GetMapping("/employee/project/{projectId}")
	public List<Employee> getEmployeeListByProjectId(@PathVariable Long projectId){
		logger.info("API hit: /api/manager/employee/project/{projectId} method:GET");
		List<Employee> employeeList = managerService.getAllEmployeeByProject(projectId);
		return employeeList;
	}
	
	@GetMapping("/request/manager/{managerId}")
	public List<Request> getRequestByManagerId(@PathVariable Long managerId){
		List<Request> requestList = managerService.getAllRequestByManager(managerId);
		return requestList;
	}
	
	@GetMapping("/employee/team")
	public List<Employee> getMyEmployee(Principal principal){
		return managerService.getAllEmployeeByManager(principal);
	}
}
