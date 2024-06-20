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
import com.assignment.employeeManagement.dto.ManagerDTO;
import com.assignment.employeeManagement.dto.ProjectDTO;
import com.assignment.employeeManagement.dto.UserAddDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.exception.ResourceNotFoundException;
import com.assignment.employeeManagement.payload.response.Response;
import com.assignment.employeeManagement.service.AdminService;
import com.assignment.employeeManagement.service.EmployeeService;
import com.assignment.employeeManagement.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RestController
@CrossOrigin
@RequestMapping("api/admin")
public class AdminController {

	private static final Logger logger = LogManager.getLogger(AdminController.class);
	
	@Autowired
	private EmployeeService employeeService;
	
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private UserService userService;
	
	@PostMapping("/employee")
	public ResponseEntity<Employee> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
		logger.info("API hit: /api/admin/employee method: POST body: "+employeeDTO);
	
			Employee employee = adminService.addEmployee(employeeDTO);
			return ResponseEntity.status(201).body(employee);
				
	}
	
	@GetMapping("/employee")
	public ResponseEntity<List<Employee>> fetchAllEmployee(){
		logger.info("API hit: /api/admin/employee");

			List<Employee> employeeList = employeeService.getAllEmployees();
			return ResponseEntity.status(200).body(employeeList);

	}
	
	
	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<Employee> fetchEmployeeById(@PathVariable Long employeeId) {
		logger.info("API hit: /api/admin/employee/{employeeId} method:GET");
			Employee employee = adminService.getEmployeeById(employeeId);
			return ResponseEntity.status(200).body(employee);	
	}

	@DeleteMapping("/employee/{employeeId}")
	public ResponseEntity<Response> deleteEmployeeById(@PathVariable Long employeeId){
		logger.info("API hit: /api/admin/employee/{employeeId} method:Delete");
			adminService.deleteEmployee(employeeId);
			return ResponseEntity.status(200).body(new Response("Successfully deleted"));
	}
	
	
	@PutMapping("/employee")
	public ResponseEntity<Employee> updateEmployee(@RequestBody EmployeeDTO employeeDTO){
		logger.info("API hit: /api/admin/employee/{employeeId} method:PUT body: "+employeeDTO);

			Employee employee = adminService.updateEmployee(employeeDTO);
			return ResponseEntity.ok(employee);
	}
	
	
    // Adding new project
    @PostMapping("/project")
    public ResponseEntity<Project> addProject(@RequestBody ProjectDTO projectDTO) {
    	logger.info("API hit: /api/admin/project method:POST");
        	 Project project = adminService.addProject(projectDTO);
    		return ResponseEntity.status(201).body(project);
    }
    
    //Fetch all projects
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProject(){
    	logger.info("API hit: /api/admin/projects method:GET");
    		List<Project> projectList =  adminService.getAllProjects();
			return ResponseEntity.status(200).body(projectList);
    }
  
    
    //assign project to employee
    @PutMapping("/employee/{employeeId}/assignProject/{projectId}")
    public ResponseEntity<Employee> assignProject(@PathVariable Long employeeId, @PathVariable Long projectId) {
    	logger.info("API hit: /api/admin/employee/{employeeId}/assignProject/{projectId} method:PUT");
    		Employee employee = adminService.assignProjectToEmployee(employeeId, projectId);
			return ResponseEntity.status(200).body(employee);	
    }
    
    @PutMapping("/employee/{employeeId}/unassignProject")
    public ResponseEntity<Employee> unassignProject(@PathVariable Long employeeId) {
    	logger.info("API hit: /api/admin/employee/{employeeId}/unassignProject method:PUT");
    		Employee employee = adminService.unassignProjectFromEmployee(employeeId);
			return ResponseEntity.status(200).body(employee);

    }
    
    @PutMapping("/request/{requestId}/approve")
    public ResponseEntity<Request> approveProject(@PathVariable Long requestId) {
    	logger.info("API hit: /api/admin/request/{requestId}/approve method:PUT");
    		Request request= adminService.approveRequest(requestId);
			return ResponseEntity.status(200).body(request);

    }
    
    @PutMapping("/request/{requestId}/reject")
    public ResponseEntity<Request> rejectRequest(@PathVariable Long requestId) {
    	logger.info("API hit: /api/admin/request/{requestId}/reject method:PUT");
    		Request request = adminService.rejectRequest(requestId);
			return ResponseEntity.status(200).body(request);
    }
    

	@PostMapping("/user")
	public ResponseEntity<User> saveUser(@RequestBody UserAddDTO userAddDTO) {
		logger.info("API hit: /api/admin/user method:POST body: "+userAddDTO);
			User user = userService.addUser(userAddDTO);
			return ResponseEntity.status(201).body(user);	
	}
    
    @GetMapping("/request")
    public ResponseEntity<List<Request>> getAllRequest(){
    	logger.info("API hit: /api/admin/request method:GET");
    		List<Request> requestList = adminService.getAllRequest();
			return ResponseEntity.status(200).body(requestList);
    }
    
    @GetMapping("/manager")
    public ResponseEntity<List<Manager>> getAllManager(){
    		List<Manager> managerList = adminService.getAllManager();
			return ResponseEntity.status(200).body(managerList);
    }
    
    @PostMapping("/manager")
    public ResponseEntity<Manager> addManager(@RequestBody ManagerDTO managerDTO) {
    	logger.info("API hit: /api/admin/manager method:POST");
    		Manager manager = adminService.addmanager(managerDTO);
			return ResponseEntity.status(201).body(manager);
    }
}
