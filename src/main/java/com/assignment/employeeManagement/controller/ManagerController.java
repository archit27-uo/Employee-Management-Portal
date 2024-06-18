package com.assignment.employeeManagement.controller;

import java.security.Principal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.employeeManagement.dto.ManagerInfoDTO;
import com.assignment.employeeManagement.dto.RequestDTO;
import com.assignment.employeeManagement.dto.UserLoginDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.payload.response.Response;
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

			List<Manager> managerList = managerService.getAllManagers();
			return ResponseEntity.status(200).body(managerList);
	}
	
	//All projects list
	@GetMapping("/project")
	public ResponseEntity<List<Project>> getAllProject(){
		logger.info("API hit: /api/manager/project method:GET");
			List<Project> projectList =  managerService.getAllProjects();
			return ResponseEntity.status(200).body(projectList);
	}
	
	//Filter unassigned employees
	@GetMapping("/employee/unassigned")
	public ResponseEntity<List<Employee>> getUnassignedEmployees(){
		logger.info("API hit: /api/manager/employee/unassigned method:GET");
			List<Employee> employeeList =  managerService.getUnassignedEmployees();
			return ResponseEntity.status(200).body(employeeList);
	}
	
	//Filter employee based on skills
	@GetMapping("/employee/filter")
	public ResponseEntity<List<Employee>> filterAllEmployeesBySkills(@RequestParam List<String> skills){
		logger.info("API hit: /api/manager/employee/filter method:GET");
			List<Employee> employeeList =  managerService.filterEmployeesBySkills(skills);
			return ResponseEntity.status(200).body(employeeList);
	}
	
	@PostMapping("/request/employees")
    public ResponseEntity<Request> requestEmployeesForProject(@RequestBody RequestDTO requestDTO) {
		logger.info("API hit: /api/manager/employee method:POST body: "+requestDTO);
			Request request = managerService.requestEmployeesForProject(requestDTO);
			return ResponseEntity.status(200).body(request);
    }
	
	@GetMapping("/info")
	public ResponseEntity<ManagerInfoDTO> getManagerInfo(Principal principal) {
		logger.info("API hit: /api/manager/info method:GET");
			ManagerInfoDTO manager = managerService.getManagerInfo(principal);
			return ResponseEntity.status(200).body(manager);
	}
	
	@GetMapping("/employee/project/{projectId}")
	public ResponseEntity<List<Employee>> getEmployeeListByProjectId(@PathVariable Long projectId){
		logger.info("API hit: /api/manager/employee/project/{projectId} method:GET");
			List<Employee> employeeList = managerService.getAllEmployeeByProject(projectId);
			return ResponseEntity.status(200).body(employeeList);
	}
	
	@GetMapping("/request/manager/{managerId}")
	public ResponseEntity<List<Request>> getRequestByManagerId(@PathVariable Long managerId){
		logger.info("API hit: /api/manager/request/manager/{managerId} method:GET");
			List<Request> requestList = managerService.getAllRequestByManager(managerId);
			return ResponseEntity.status(200).body(requestList);
	}
	
	@GetMapping("/employee/team")
	public ResponseEntity<List<Employee>> getMyEmployee(Principal principal){
		logger.info("API hit: /api/manager/employee/team method:GET");
			List<Employee> employeeList = managerService.getAllEmployeeByManager(principal);
			return ResponseEntity.status(200).body(employeeList);
	}
	
	@PutMapping("/changePassword")
	public ResponseEntity<Response> changePassword(Principal principal, @RequestBody UserLoginDTO changePasswordFiled){
		logger.info("API hit: /api/manager/changePassword method:PUT Principal: "+principal);
		String password = changePasswordFiled.getUserPassword();
		managerService.changePassword(principal, password);
		Response response = new Response("Changed password successfully");
		return ResponseEntity.status(200).body(response);
	}
	
}
