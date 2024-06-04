package com.assignment.employeeManagement.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.controller.AdminController;
import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.dto.ManagerDTO;
import com.assignment.employeeManagement.dto.ProjectDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.model.RequestStatus;
import com.assignment.employeeManagement.model.RequestType;
import com.assignment.employeeManagement.repository.EmployeeRepository;
import com.assignment.employeeManagement.repository.ManagerRepository;
import com.assignment.employeeManagement.repository.ProjectRepository;
import com.assignment.employeeManagement.repository.RequestRepository;
import com.assignment.employeeManagement.repository.UserLoginRepo;

@Service
public class AdminServiceIMPL implements AdminService{

	private static final Logger logger = LogManager.getLogger(AdminController.class);
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private UserLoginRepo userRepository;
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private RequestRepository requestRepository;
	
	
	
	@Override
	public Employee addEmployee(EmployeeDTO employeeDTO) {
		int userId = employeeDTO.getUserId();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
		
	    Long managerId = employeeDTO.getManagerId();
	    Manager manager = null;
	    if (managerId != null) {
	        manager = managerRepository.findById(managerId)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid manager ID"));
	    }

	    Long projectId = employeeDTO.getProjectId();
	    Project project = null;
	    if (projectId != null) {
	        project = projectRepository.findById(projectId)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));
	    }
		
        
        Employee employee = new Employee(
				employeeDTO.getEmployeeId(),
				user,
				employeeDTO.getFullName(),
				project,
				manager,
				employeeDTO.getSkills()
				);
		
		employeeRepository.save(employee);
		return employee;
	}

	
	
	@Override
	public Project addProject(ProjectDTO projectDTO)
	{
		Long managerId = projectDTO.getManagerId();
		Manager manager = null;
		if(managerId!=null) {
			manager = managerRepository.findById(managerId)
			.orElseThrow(()->new IllegalArgumentException("Invalid manager Id"));
		}
		
		Project project = new Project(
				projectDTO.getProjectId(),
				projectDTO.getProjectName(),
				manager
				);
		
		return projectRepository.save(project);
	}

	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}

	@Override
	public List<Project> getAllProjects() {
		List<Project> projectList = projectRepository.findAll();
		return projectList;
	}

	@Override
	public Employee assignProjectToEmployee(Long employeeId, Long projectId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(()->new IllegalArgumentException("Invalid Employee ID"));
		
		Project project = projectRepository.findById(projectId)
				.orElseThrow(()-> new IllegalArgumentException("Inavlid Project Id"));
		
		
		employee.setProject(project);
		employee.setManager(project.getManager());
		return employeeRepository.save(employee);
	}

	@Override
	public Employee unassignProjectFromEmployee(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(()->new IllegalArgumentException("Invalid Employee ID"));
		employee.setProject(null);
	
		return employeeRepository.save(employee);
	}

	@Override
	public Request approveRequest(Long requestId) {
		Request request = requestRepository.findById(requestId)
				.orElseThrow(()-> new IllegalArgumentException("Invalid Request Id"));
		if(request.getRequestType()==RequestType.ASSIGN_EMPLOYEE) {
			int len = request.getEmployeeIds().size();
			for(int i=0; i<len; i++) {
				Employee employee = employeeRepository.findById(request.getEmployeeIds().get(i))
						.orElseThrow(()-> new IllegalArgumentException("Employee not found"));
				System.out.println(employee);
//				Project project = projectRepository.findById(request.getProjectId())
//						.orElseThrow(()-> new IllegalArgumentException("No Such Project Exist"));
//				
//				 if (employee.getProject() != null && employee.getProject().getProjectId().equals(project.getProjectId())) {
//		                System.out.println("Employee " + employee.getEmployeeId() + " is already assigned to project " + project.getProjectId());
//		            }
//				 else{
				//employee.setProject(this.assignProjectToEmployee(requestId, requestId));
				
				employee.setManager(request.getRequester());
				System.out.println(employee);
//				 try {
	                    employeeRepository.save(employee);
	                    
	                  this.assignProjectToEmployee(employee.getEmployeeId(), request.getProjectId());
	                   
//	                } catch (org.springframework.dao.DataIntegrityViolationException e) {
//	                    System.err.println("DataIntegrityViolationException: " + e.getMessage());
//	                    throw new RuntimeException("Could not assign project to employee due to unique constraint violation.");
//	                }
		         }
			
		}else {
			int len = request.getEmployeeIds().size();
			for(int i=0; i<len; i++) {
				Employee employee = employeeRepository.findById(request.getEmployeeIds().get(i))
						.orElseThrow(()-> new IllegalArgumentException("Employee not found"));
				employee.setManager(null);
				employee.setProject(null);
				employeeRepository.save(employee);
			}
		}
		request.setStatus(RequestStatus.APPROVED);
		return requestRepository.save(request);
		
	}

	@Override
	public Request rejectRequest(Long requestId) {
		Request request = requestRepository.findById(requestId)
				.orElseThrow(()-> new IllegalArgumentException("Invalid Request Id"));
		request.setStatus(RequestStatus.REJECT);
		return requestRepository.save(request);
		
	}

	@Override
	public void deleteEmployee(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(()->new IllegalArgumentException("UserId doesnot exist"));
		
		employeeRepository.deleteById(employeeId);
		userRepository.deleteById(employee.getUser().getUserId());
		System.out.println("deleted");
		
	}

	@Override
	public Employee updateEmployee(Long employeeId, EmployeeDTO employeeDTO) {
		Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));

        User user = userRepository.findById(employeeDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        employee.setUser(user);
        employee.setFullName(employeeDTO.getFullName());

        if (employeeDTO.getProjectId() != null) {
            Project project = projectRepository.findById(employeeDTO.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));
            employee.setProject(project);
        } else {
            employee.setProject(null);
        }

        if (employeeDTO.getManagerId() != null) {
            Manager manager = managerRepository.findById(employeeDTO.getManagerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid manager ID"));
            employee.setManager(manager);
        } else {
            employee.setManager(null);
        }

        employee.setSkills(employeeDTO.getSkills());

        employeeRepository.save(employee);
        return employee;
	}



	@Override
	public List<Request> getAllRequest() {
		List<Request> requestList = requestRepository.findAll();
		return requestList;
	}



	@Override
	public Manager addmanager(ManagerDTO managerDTO) {
		Manager manager = new Manager();
		manager.setUser(userRepository.findById(managerDTO.getUserId())
				.orElseThrow(()->new IllegalArgumentException("use not found")));
		return managerRepository.save(manager);
	}



	@Override
	public List<Manager> getAllManager() {
		List<Manager> managerList = managerRepository.findAll();
		return managerList;
	}

	
}
