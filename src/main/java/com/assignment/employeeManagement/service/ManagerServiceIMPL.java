package com.assignment.employeeManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
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
	public Request requestEmployeesForProject(String email, Long projectId, List<Long> employeeIds) {
		Manager requester =managerRepository.findByUser(userRepository.findByUserEmail(email));
		Request request = new Request();
		request.setRequester(requester);
		request.setRequestType(RequestType.EMPLOYEE);
		request.setRequestDetails("Project ID: "+projectId+", Employee IDs: "+employeeIds);
		request.setStatus(RequestStatus.PENDING);
		return requestRepository.save(request);
	}

}