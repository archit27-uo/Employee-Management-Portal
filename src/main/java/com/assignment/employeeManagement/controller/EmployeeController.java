package com.assignment.employeeManagement.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.dto.UserLoginDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.payload.response.LoginResponse;
import com.assignment.employeeManagement.payload.response.Response;
import com.assignment.employeeManagement.service.EmployeeService;

@RestController
@CrossOrigin
@RequestMapping("api/employee")
public class EmployeeController {
	
	private static final Logger logger = LogManager.getLogger(EmployeeController.class);
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/all")
	public ResponseEntity<List<Employee>> fetchAllEmployee(){
		logger.info("API hit: /api/employee/all method:GET");
			List<Employee> employeeList = employeeService.getAllEmployees();
			return ResponseEntity.status(200).body(employeeList);
	}
	
	
	@PutMapping("/skills")
	public ResponseEntity<Employee> updateSkills(Principal principal, @RequestBody List<String> skills){
		logger.info("API hit: /api/employee/skills method:PUT body: "+skills);
			Employee employee = employeeService.updateSkills(principal, skills);
			return ResponseEntity.status(200).body(employee);

	}
	
	
	@GetMapping("/info")
	public ResponseEntity<Employee> getInfo(Principal principal) {
		logger.info("API hit: /api/employee/info method:GET Principal: "+principal);
			Employee employee = employeeService.getEmployeeInfo(principal);
			return ResponseEntity.status(200).body(employee);

	}
	
	@PutMapping("/changePassword")
	public ResponseEntity<Response> changePassword(Principal principal, @RequestBody UserLoginDTO userLoginDTO){
		logger.info("API hit: /api/employee/changePassword method:PUT Principal: "+principal);
		String password = userLoginDTO.getUserPassword();
		employeeService.changePassword(principal, password);
		Response response = new Response("Changed password successfully");
		return ResponseEntity.status(200).body(response);
	}
	
	
	@GetMapping("/manager")
	public ResponseEntity<List<Manager>> getAllManager(){
		logger.info("API hit: /api/employee/manager method:GET");
		List<Manager> managerList = employeeService.getAllManagers();
		return ResponseEntity.status(200).body(managerList);
		
	}
//	@PutMapping("/employee/{employeeId}")
//	public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId,@RequestBody EmployeeDTO employeeDTO){
//		logger.info("API hit: /api/employee/employee/{employeeID} method:PUT body: "+employeeDTO);
////		try {
//			Employee employee =  employeeService.updateEmployee(employeeId, employeeDTO);
//			return ResponseEntity.status(200).body(employee);
////		}catch(Exception e) {
////			return ResponseEntity.badRequest().body(null);
////			}
//	}
}
