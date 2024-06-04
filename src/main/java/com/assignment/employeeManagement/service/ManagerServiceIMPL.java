package com.assignment.employeeManagement.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.dto.ManagerDTO;
import com.assignment.employeeManagement.dto.ManagerInfoDTO;
import com.assignment.employeeManagement.dto.RequestDTO;
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
public class ManagerServiceIMPL implements ManagerService {

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
	public List<Employee> getAllEmployees() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}

	@Override
	public List<Manager> getAllManagers() {
		List<Manager> managerList = managerRepository.findAll();
		return managerList;
	}

	@Override
	public List<Project> getAllProjects() {
		List<Project> projectList = projectRepository.findAll();
		return projectList;
	}

	@Override
	public List<Employee> filterEmployeesBySkills(List<String> skills) {
		List<Employee> employeeList = employeeRepository.findBySkillsIn(skills);
		return employeeList;
	}

	@Override
	public List<Employee> getUnassignedEmployees() {
		List<Employee> employeeList = employeeRepository.findByProjectIsNull();
		return employeeList;
	}

	@Override
	public Request requestEmployeesForProject(RequestDTO requestDTO) {
		Manager requester = managerRepository.findByManagerId(requestDTO.getRequesterId());
		Request request = new Request();
		request.setRequester(requester);
		request.setRequestType(requestDTO.getRequestType());
		request.setProjectId(requestDTO.getProjectId());
		request.setEmployeeIds(requestDTO.getEmployeeIds());
		request.setRequestDetails(
				requestDTO.getRequestDetails()!=null?
						requestDTO.getRequestDetails():
						"Project ID: "+requestDTO.getProjectId()+", Employee IDs: "+requestDTO.getEmployeeIds()
						);
		request.setStatus(RequestStatus.PENDING);
		return requestRepository.save(request);
	}

	@Override
	public ManagerInfoDTO getManagerInfo(Principal principal) {
		String email = principal.getName();
	    User user = userRepository.findByUserEmail(email);
	    if (user == null) {
	        throw new IllegalArgumentException("User not found");
	    }

	    Manager manager = managerRepository.findByUser(user);
	    if (manager == null) {
	        throw new IllegalArgumentException("Employee not found");
	    }
	    ManagerInfoDTO managerInfoDTO = new ManagerInfoDTO();
	    managerInfoDTO.setManagerId(manager.getManagerId());
	    managerInfoDTO.setFullName(manager.getUser().getUserName());
	    managerInfoDTO.setProjectList(projectRepository.findAllByManager(manager));
	    managerInfoDTO.setEmployeeList(employeeRepository.findAllByManager(manager));
	    

	    return managerInfoDTO;
	}

	@Override
	public List<Employee> getAllEmployeeByProject(Long projectId) {
		Project project = projectRepository.findById(projectId).orElseThrow(()->new IllegalArgumentException("Project not found"));
		List<Employee> employeeList = employeeRepository.findAllByProject(project);
				
		return employeeList;
	}


	}

