package com.assignment.employeeManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.dto.ProjectDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.model.RequestStatus;
import com.assignment.employeeManagement.repository.EmployeeRepository;
import com.assignment.employeeManagement.repository.ManagerRepository;
import com.assignment.employeeManagement.repository.ProjectRepository;
import com.assignment.employeeManagement.repository.RequestRepository;
import com.assignment.employeeManagement.repository.UserLoginRepo;

@Service
public class AdminServiceIMPL implements AdminService{

	
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
		employeeRepository.deleteById(employeeId);
		
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

}
