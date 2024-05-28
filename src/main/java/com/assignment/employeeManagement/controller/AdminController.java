package com.assignment.employeeManagement.controller;

import java.util.List;
import java.util.Optional;

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
import com.assignment.employeeManagement.dto.ProjectDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.service.AdminService;
import com.assignment.employeeManagement.service.EmployeeService;

@RestController
@CrossOrigin
@RequestMapping("api/admin")
public class AdminController {

	
	@Autowired
	private EmployeeService employeeService;
	
    @Autowired
    private AdminService adminService;
	
	@PostMapping("/employee")
	public Employee addEmployee(@RequestBody EmployeeDTO employeeDTO) {
		Employee employee = employeeService.addEmployee(employeeDTO);
		return employee;
	}
	
	@GetMapping("/employee")
	public List<Employee> fetchAllEmployee(){
		List<Employee> employeeList = employeeService.getAllEmployees();
		return employeeList;
	}
	
	
	@GetMapping("/employee/{employeeId}")
	public Optional<Employee> fetchEmployeeById(@PathVariable Long employeeId) {
		Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
		return employee;
	}

	@DeleteMapping("/employee/{employeeId}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable Long employeeId){
		try {
		employeeService.deleteEmployee(employeeId);
		return ResponseEntity.ok("Successfully Deleted");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("Error");
		}
	}
	
	
	@PutMapping("/employee/{employeeId}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId,@RequestBody EmployeeDTO employeeDTO){
		Employee employee =  employeeService.updateEmployee(employeeId, employeeDTO);
		return ResponseEntity.ok(employee);
		
	}
	
    // Adding new project
    @PostMapping("/project")
    public Project addProject(@RequestBody ProjectDTO projectDTO) {
        return adminService.addProject(projectDTO);
    }
    
    //Fetch all projects
    @GetMapping("/projects")
    public List<Project> getAllProject(){
    	return adminService.getAllProjects();
    }
    
    //assign project to employee
    @PutMapping("/employee/{employeeId}/assignProject/{projectId}")
    public Employee assignProject(@PathVariable Long employeeId, @PathVariable Long projectId) {
    	Employee employee = adminService.assignProjectToEmployee(employeeId, projectId);
    	return employee;
    	
    }
    
    @PutMapping("/employee/{employeeId}/unassignProject")
    public Employee unassignProject(@PathVariable Long employeeId) {
    	Employee employee = adminService.unassignProjectFromEmployee(employeeId);
    	return employee;
    }
    
    @PutMapping("/request/{requestId}/approve")
    public Request approveProject(@PathVariable Long requestId) {
    	Request request= adminService.approveRequest(requestId);
    	return request;
    }
    
    @PutMapping("/request/{requestId}/reject")
    public Request rejectRequest(@PathVariable Long requestId) {
    	Request request = adminService.rejectRequest(requestId);
    	return request;
    }
    
    
}
