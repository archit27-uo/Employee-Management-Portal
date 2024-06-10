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

	private static final Logger logger = LogManager.getLogger(ManagerController.class);
	
	@Autowired
	private ManagerService managerService;
	
	
	@GetMapping("/employee")
	public List<Employee> getAllEmployee(){
		logger.info("API hit: /api/manager/employee method:GET");
		return managerService.getAllEmployees();
	}
	
	@GetMapping("/manager")
	public ResponseEntity<List<Manager>> getAllManager(){
		logger.info("API hit: /api/manager/manager method:GET");
//		try {
			List<Manager> managerList = managerService.getAllManagers();
			return ResponseEntity.status(200).body(managerList);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
	}
	
	//All projects list
	@GetMapping("/project")
	public ResponseEntity<List<Project>> getAllProject(){
		logger.info("API hit: /api/manager/project method:GET");
		
//		try {
			List<Project> projectList =  managerService.getAllProjects();
			return ResponseEntity.status(200).body(projectList);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
	}
	
	//Filter unassigned employees
	@GetMapping("/employee/unassigned")
	public ResponseEntity<List<Employee>> getUnassignedEmployees(){
		logger.info("API hit: /api/manager/employee/unassigned method:GET");
//		try {
			List<Employee> employeeList =  managerService.getUnassignedEmployees();
			return ResponseEntity.status(200).body(employeeList);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
	}
	
	//Filter employee based on skills
	@GetMapping("/employee/filter")
	public ResponseEntity<List<Employee>> filterAllEmployeesBySkills(@RequestParam List<String> skills){
		logger.info("API hit: /api/manager/employee/filter method:GET");
//		try {
			List<Employee> employeeList =  managerService.filterEmployeesBySkills(skills);
			return ResponseEntity.status(200).body(employeeList);
//		}catch(Exception e) {
//				return ResponseEntity.badRequest().body(null);
//			}
	}
	
	@PostMapping("/request/employees")
    public ResponseEntity<Request> requestEmployeesForProject(@RequestBody RequestDTO requestDTO) {
		logger.info("API hit: /api/manager/employee method:POST body: "+requestDTO);
//		try {
			Request request = managerService.requestEmployeesForProject(requestDTO);
			return ResponseEntity.status(200).body(request);
//		}catch(Exception e) {
//				return ResponseEntity.badRequest().body(null);
//			}
    }
	
	@GetMapping("/info")
	public ResponseEntity<ManagerInfoDTO> getManagerInfo(Principal principal) {
		logger.info("API hit: /api/manager/info method:GET");
//		try {
			ManagerInfoDTO manager = managerService.getManagerInfo(principal);
			return ResponseEntity.status(200).body(manager);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
	}
	
	@GetMapping("/employee/project/{projectId}")
	public ResponseEntity<List<Employee>> getEmployeeListByProjectId(@PathVariable Long projectId){
		logger.info("API hit: /api/manager/employee/project/{projectId} method:GET");
//		try {
			List<Employee> employeeList = managerService.getAllEmployeeByProject(projectId);
			return ResponseEntity.status(200).body(employeeList);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
	}
	
	@GetMapping("/request/manager/{managerId}")
	public ResponseEntity<List<Request>> getRequestByManagerId(@PathVariable Long managerId){
		logger.info("API hit: /api/manager/request/manager/{managerId} method:GET");
//		try {
			List<Request> requestList = managerService.getAllRequestByManager(managerId);
			return ResponseEntity.status(200).body(requestList);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
	}
	
	@GetMapping("/employee/team")
	public ResponseEntity<List<Employee>> getMyEmployee(Principal principal){
		logger.info("API hit: /api/manager/employee/team method:GET");
//		try {
			List<Employee> employeeList = managerService.getAllEmployeeByManager(principal);
			return ResponseEntity.status(200).body(employeeList);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
	}
}
