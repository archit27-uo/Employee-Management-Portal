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
//		try {
			List<Employee> employeeList = employeeService.getAllEmployees();
			return ResponseEntity.status(200).body(employeeList);
//		}catch(Exception e) {
//				return ResponseEntity.badRequest().body(null);
//			}
	}
	
	
	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<Employee> fetchEmployeeById(@PathVariable Long employeeId) {
		logger.info("API hit: /api/admin/employee/{employeeId} method:GET");
//		try {
			Employee employee = adminService.getEmployeeById(employeeId);
			return ResponseEntity.status(200).body(employee);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
		
	}

	@DeleteMapping("/employee/{employeeId}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable Long employeeId){
		logger.info("API hit: /api/admin/employee/{employeeId} method:Delete");
//		try {
			adminService.deleteEmployee(employeeId);
			return ResponseEntity.status(200).body("Successfully Deleted");
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body("Error : "+e);
//		}
	}
	
	
	@PutMapping("/employee/{employeeId}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId,@RequestBody EmployeeDTO employeeDTO){
		logger.info("API hit: /api/admin/employee/{employeeId} method:PUT body: "+employeeDTO);
//		try {
			Employee employee = adminService.updateEmployee(employeeId, employeeDTO);
			return ResponseEntity.ok(employee);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
//		
		
	}
	
	
    // Adding new project
    @PostMapping("/project")
    public ResponseEntity<Project> addProject(@RequestBody ProjectDTO projectDTO) {
    	logger.info("API hit: /api/admin/project method:POST");
//        try {
        	 Project project = adminService.addProject(projectDTO);
    		return ResponseEntity.status(200).body(project);
//    	}catch(Exception e) {
//    		return ResponseEntity.badRequest().body(null);
//    		}
    }
    
    //Fetch all projects
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProject(){
    	logger.info("API hit: /api/admin/projects method:GET");
    	
//    	try {
    		List<Project> projectList =  adminService.getAllProjects();
			return ResponseEntity.status(200).body(projectList);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
    }
  
    
    //assign project to employee
    @PutMapping("/employee/{employeeId}/assignProject/{projectId}")
    public ResponseEntity<Employee> assignProject(@PathVariable Long employeeId, @PathVariable Long projectId) {
    	logger.info("API hit: /api/admin/employee/{employeeId}/assignProject/{projectId} method:PUT");
//    	try {
    		Employee employee = adminService.assignProjectToEmployee(employeeId, projectId);
			return ResponseEntity.status(200).body(employee);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}	
    }
    
    @PutMapping("/employee/{employeeId}/unassignProject")
    public ResponseEntity<Employee> unassignProject(@PathVariable Long employeeId) {
    	logger.info("API hit: /api/admin/employee/{employeeId}/unassignProject method:PUT");
//    	try {
    		Employee employee = adminService.unassignProjectFromEmployee(employeeId);
			return ResponseEntity.status(200).body(employee);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
    }
    
    @PutMapping("/request/{requestId}/approve")
    public ResponseEntity<Request> approveProject(@PathVariable Long requestId) {
    	logger.info("API hit: /api/admin/request/{requestId}/approve method:PUT");
//    	try {
    		Request request= adminService.approveRequest(requestId);
			return ResponseEntity.status(200).body(request);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//		}
    }
    
    @PutMapping("/request/{requestId}/reject")
    public ResponseEntity<Request> rejectRequest(@PathVariable Long requestId) {
    	logger.info("API hit: /api/admin/request/{requestId}/reject method:PUT");
//    	try {
    		Request request = adminService.rejectRequest(requestId);
			return ResponseEntity.status(200).body(request);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
    }
    

	@PostMapping("/user")
	public ResponseEntity<User> saveUser(@RequestBody UserAddDTO userAddDTO) {
		logger.info("API hit: /api/admin/user method:POST body: "+userAddDTO);
//		try {
			User user = userService.addUser(userAddDTO);
			return ResponseEntity.status(201).body(user);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}	
	}
    
    @GetMapping("/request")
    public ResponseEntity<List<Request>> getAllRequest(){
    	logger.info("API hit: /api/admin/request method:GET");
//    	try {
    		List<Request> requestList = adminService.getAllRequest();
			return ResponseEntity.status(200).body(requestList);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
    }
    
    @GetMapping("/manager")
    public ResponseEntity<List<Manager>> getAllManager(){
    	logger.info("API hit: /api/admin/manager method:GET");
//    	try {
    		List<Manager> managerList = adminService.getAllManager();
			return ResponseEntity.status(200).body(managerList);
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body(null);
//			}
    }
    
    @PostMapping("/manager")
    public ResponseEntity<Manager> addManager(@RequestBody ManagerDTO managerDTO) {
    	logger.info("API hit: /api/admin/manager method:POST");
//    	try {
    		Manager manager = adminService.addmanager(managerDTO);
			return ResponseEntity.status(201).body(manager);
//		}catch(Exception e) {
//				return ResponseEntity.badRequest().body(null);
//			}
    }
}
