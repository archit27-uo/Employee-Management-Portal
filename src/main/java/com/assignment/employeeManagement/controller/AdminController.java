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
	public Employee addEmployee(@RequestBody EmployeeDTO employeeDTO) {
		logger.info("API hit: /api/admin/employee method: POST body: "+employeeDTO);
		Employee employee = employeeService.addEmployee(employeeDTO);
		return employee;
	}
	
	@GetMapping("/employee")
	public List<Employee> fetchAllEmployee(){
		logger.info("API hit: /api/admin/employee");
		List<Employee> employeeList = employeeService.getAllEmployees();
		return employeeList;
	}
	
	
	@GetMapping("/employee/{employeeId}")
	public Optional<Employee> fetchEmployeeById(@PathVariable Long employeeId) {
		logger.info("API hit: /api/admin/employee/{employeeId} method:GET");
		Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
		return employee;
	}

	@DeleteMapping("/employee/{employeeId}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable Long employeeId){
		logger.info("API hit: /api/admin/employee/{employeeId} method:Delete");
		try {
		adminService.deleteEmployee(employeeId);
		return ResponseEntity.status(200).body("Successfully Deleted");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("Error");
		}
	}
	
	
	@PutMapping("/employee/{employeeId}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId,@RequestBody EmployeeDTO employeeDTO){
		logger.info("API hit: /api/admin/employee/{employeeId} method:PUT");
		Employee employee =  employeeService.updateEmployee(employeeId, employeeDTO);
		return ResponseEntity.ok(employee);
		
	}
	
    // Adding new project
    @PostMapping("/project")
    public Project addProject(@RequestBody ProjectDTO projectDTO) {
    	logger.info("API hit: /api/admin/project method:POST");
        return adminService.addProject(projectDTO);
    }
    
    //Fetch all projects
    @GetMapping("/projects")
    public List<Project> getAllProject(){
    	logger.info("API hit: /api/admin/projects method:GET");
    	return adminService.getAllProjects();
    }
    
    //assign project to employee
    @PutMapping("/employee/{employeeId}/assignProject/{projectId}")
    public Employee assignProject(@PathVariable Long employeeId, @PathVariable Long projectId) {
    	logger.info("API hit: /api/admin/employee/{employeeId}/assignProject/{projectId} method:PUT");
    	Employee employee = adminService.assignProjectToEmployee(employeeId, projectId);
    	return employee;
    	
    }
    
    @PutMapping("/employee/{employeeId}/unassignProject")
    public Employee unassignProject(@PathVariable Long employeeId) {
    	logger.info("API hit: /api/admin/employee/{employeeId}/unassignProject method:PUT");
    	Employee employee = adminService.unassignProjectFromEmployee(employeeId);
    	return employee;
    }
    
    @PutMapping("/request/{requestId}/approve")
    public Request approveProject(@PathVariable Long requestId) {
    	logger.info("API hit: /api/admin/request/{requestId}/approve method:PUT");
    	Request request= adminService.approveRequest(requestId);
    	return request;
    }
    
    @PutMapping("/request/{requestId}/reject")
    public Request rejectRequest(@PathVariable Long requestId) {
    	logger.info("API hit: /api/admin/request/{requestId}/reject method:PUT");
    	Request request = adminService.rejectRequest(requestId);
    	return request;
    }
    

	@PostMapping("/user")
	public User saveUser(@RequestBody UserAddDTO userAddDTO) {
		logger.info("API hit: /api/admin/user method:POST body: "+userAddDTO);
		User user = userService.addUser(userAddDTO);
		return user;
	}
    
    @GetMapping("/request")
    public List<Request> getAllRequest(){
    	logger.info("API hit: /api/admin/request method:GET");
    	List<Request> requestList = adminService.getAllRequest();
    	return requestList;
    }
    
    @GetMapping("/manager")
    public List<Manager> getAllManager(){
    	logger.info("API hit: /api/admin/manager method:GET");
    	List<Manager> managerList = adminService.getAllManager();
    	return managerList;
    }
    
    @PostMapping("/manager")
    public Manager addManager(@RequestBody ManagerDTO managerDTO) {
    	logger.info("API hit: /api/admin/manager method:POST");
    	Manager manager = adminService.addmanager(managerDTO);
    	return manager;
    }
}
