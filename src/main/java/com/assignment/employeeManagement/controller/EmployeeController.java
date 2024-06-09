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
import com.assignment.employeeManagement.entity.Employee;
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
//		try {
			List<Employee> employeeList = employeeService.getAllEmployees();
			return ResponseEntity.status(200).body(employeeList);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
	}
	
	
	@PutMapping("/skills")
	public ResponseEntity<Employee> updateSkills(Principal principal, @RequestBody List<String> skills){
		logger.info("API hit: /api/employee/skills method:PUT body: "+skills);
//		try {
			Employee employee = employeeService.updateSkills(principal, skills);
			return ResponseEntity.status(200).body(employee);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
	}
	
	
	@GetMapping("/info")
	public ResponseEntity<Employee> getInfo(Principal principal) {
		logger.info("API hit: /api/employee/info method:GET Principal: "+principal);
//		try {
			System.out.println("inside controller info");
			Employee employee = employeeService.getEmployeeInfo(principal);
			return ResponseEntity.status(200).body(employee);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
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
