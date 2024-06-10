package com.assignment.employeeManagement.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.controller.AdminController;
import com.assignment.employeeManagement.dto.ManagerDTO;
import com.assignment.employeeManagement.dto.ManagerInfoDTO;
import com.assignment.employeeManagement.dto.RequestDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.exception.EmployeeAlreadyAssignedException;
import com.assignment.employeeManagement.exception.ResourceNotFoundException;
import com.assignment.employeeManagement.model.RequestStatus;
import com.assignment.employeeManagement.model.RequestType;
import com.assignment.employeeManagement.repository.EmployeeRepository;
import com.assignment.employeeManagement.repository.ManagerRepository;
import com.assignment.employeeManagement.repository.ProjectRepository;
import com.assignment.employeeManagement.repository.RequestRepository;
import com.assignment.employeeManagement.repository.UserLoginRepo;


@Service
public class ManagerServiceIMPL implements ManagerService {

	private static final Logger logger = LogManager.getLogger(ManagerServiceIMPL.class);
	
	
	@Autowired
	private EmployeeService employeeService;
	
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
			return employeeService.getAllEmployees();

	}

	@Override
	public List<Manager> getAllManagers() {
		try {
			List<Manager> managerList = managerRepository.findAll();
			return managerList;
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }	
	}

	@Override
	public List<Project> getAllProjects() {
		try {
			List<Project> projectList = projectRepository.findAll();
			return projectList;	
		}catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
	}

	@Override
	public List<Employee> filterEmployeesBySkills(List<String> skills) {
		try {
			List<Employee> employeeList = employeeRepository.findBySkillsIn(skills);
			return employeeList;
		}catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
	}

	@Override
	public List<Employee> getUnassignedEmployees() {
		try {
			List<Employee> employeeList = employeeRepository.findByProjectIsNull();
			return employeeList;	
		}catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
	}

	@Override
	public Request requestEmployeesForProject(RequestDTO requestDTO) {
		try {
			Manager requester = managerRepository.findById(requestDTO.getRequesterId())
					.orElseThrow(()-> new ResourceNotFoundException("Manager not found with manager id: "+requestDTO.getRequesterId()));
			Project project = projectRepository.findById(requestDTO.getProjectId())
					.orElseThrow(()-> new ResourceNotFoundException("Project not found"));
			if(requester.getManagerId()!=project.getManager().getManagerId()) {
				throw new IllegalArgumentException("This project is already assigned to other manager");
			}
			if(requestDTO.getRequestType()==RequestType.ASSIGN_EMPLOYEE) {
				int len = requestDTO.getEmployeeIds().size();
				for(int i=0; i<len; i++) {
					Employee employee = employeeRepository.findById(requestDTO.getEmployeeIds().get(i))
							.orElseThrow(()->new ResourceNotFoundException("Employee not found"));
					if(employee.getProject()!=null) {
						throw new EmployeeAlreadyAssignedException("Employee is already assigned to a project, employee id: "+requestDTO.getEmployeeIds().get(i));
					}
				}
			}
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
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw ex;
        }
	}

	@Override
	public ManagerInfoDTO getManagerInfo(Principal principal) {
		try {
			String email = principal.getName();
		    User user = userRepository.findByUserEmail(email);
		    if (user == null) {
		        throw new ResourceNotFoundException("User not found");
		    }

		    Manager manager = managerRepository.findByUser(user)
		    		.orElseThrow(()->new ResourceNotFoundException("Manager not found with user id: "+user.getUserId()));

		    ManagerInfoDTO managerInfoDTO = new ManagerInfoDTO();
		    managerInfoDTO.setManagerId(manager.getManagerId());
		    managerInfoDTO.setFullName(manager.getUser().getUserName());
		    managerInfoDTO.setProjectList(projectRepository.findAllByManager(manager));
		    managerInfoDTO.setEmployeeList(employeeRepository.findAllByManager(manager)
		    		.orElse(new ArrayList<Employee>()));
		    return managerInfoDTO;
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
		
	}

	@Override
	public List<Employee> getAllEmployeeByProject(Long projectId) {
	try {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(()->new ResourceNotFoundException("Project not found with project Id: "+projectId));
		List<Employee> employeeList = employeeRepository.findAllByProject(project);
		return employeeList;		
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
		
	}

	@Override
	public List<Request> getAllRequestByManager(Long managerId) {
	try {
		Manager requester = managerRepository.findByManagerId(managerId);
		List<Request> requestList = requestRepository.findAllByRequester(requester);
		return requestList;	
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
		
	}

	@Override
	public List<Employee> getAllEmployeeByManager(Principal principal) {
		
	try {
		String email = principal.getName();
	    User user = userRepository.findByUserEmail(email);
	    if (user == null) {
	        throw new ResourceNotFoundException("User not found with user email "+email);
	    }
	    Manager manager = managerRepository.findByUser(user)
	    		.orElseThrow(()-> new ResourceNotFoundException("Manager not found"));
	    		
		List<Employee> employeeList = employeeRepository.findAllByManager(manager)
				.orElseThrow(()->new ResourceNotFoundException("No employee found with manager id "+manager.getManagerId()));
		return  employeeList;	
			
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
	}


	}

