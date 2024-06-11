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
import com.assignment.employeeManagement.exception.EmployeeAlreadyAssignedException;
import com.assignment.employeeManagement.exception.ManagerAlreadyAssignedException;
import com.assignment.employeeManagement.exception.ResourceNotFoundException;
import com.assignment.employeeManagement.model.RequestStatus;
import com.assignment.employeeManagement.model.RequestType;
import com.assignment.employeeManagement.repository.EmployeeRepository;
import com.assignment.employeeManagement.repository.ManagerRepository;
import com.assignment.employeeManagement.repository.ProjectRepository;
import com.assignment.employeeManagement.repository.RequestRepository;
import com.assignment.employeeManagement.repository.UserLoginRepo;

@Service
public class AdminServiceIMPL implements AdminService{

	private static final Logger logger = LogManager.getLogger(AdminServiceIMPL.class);
	
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
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private ManagerService managerService;
	
	@Override
	public Employee addEmployee(EmployeeDTO employeeDTO) {
		
		try {
			int userId = employeeDTO.getUserId();
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException("No user found with user id: " + userId));
			
			Project project = null;
			Manager manager = null;
			if (employeeDTO.getProjectId() != null) {
	            project = projectRepository.findById(employeeDTO.getProjectId())
	                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with project Id: "+employeeDTO.getProjectId()));
	          
	             manager = project.getManager();
	            if(employeeDTO.getManagerId()!= manager.getManagerId()) {
	            	throw new IllegalArgumentException("Manager Id does not match with project manager id");
	            }
	            
	        } 

	        if (employeeDTO.getManagerId() != null && project==null) {
	             manager = managerRepository.findById(employeeDTO.getManagerId())
	                    .orElseThrow(() -> new ResourceNotFoundException("No manager found with manager Id: "+employeeDTO.getManagerId()));
	            
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
			
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw ex;
        }
		
		
	}

	
	
	@Override
	public Project addProject(ProjectDTO projectDTO){
	try {
		Long managerId = projectDTO.getManagerId();
		Manager manager = null;
		if(managerId!=null) {
			manager = managerRepository.findById(managerId)
			.orElseThrow(()->new ResourceNotFoundException("No Manager found with manager Id: "+managerId));
		}
		
		Project project = new Project(
				projectDTO.getProjectId(),
				projectDTO.getProjectName(),
				manager
				);
		
		return projectRepository.save(project);		
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
	}

	@Override
	public List<Employee> getAllEmployees() {

		return employeeService.getAllEmployees();

	}

	@Override
	public List<Project> getAllProjects() {
		return managerService.getAllProjects();

	}

	@Override
	public Employee assignProjectToEmployee(Long employeeId, Long projectId) {
	try {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(()->new ResourceNotFoundException("No employee found with Employee ID: "+employeeId));
		
		Project project = projectRepository.findById(projectId)
				.orElseThrow(()-> new ResourceNotFoundException("No project found with Project Id: "+projectId));
		if(employee.getProject()!=null) {
			throw new EmployeeAlreadyAssignedException("Employee is already assigned to a project");
		}else {
			employee.setProject(project);
			employee.setManager(project.getManager());
			return employeeRepository.save(employee);	
		}

		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw ex;
        }
	}

	@Override
	public Employee unassignProjectFromEmployee(Long employeeId) {
	try {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(()->new ResourceNotFoundException("No Employee found with Employee ID: "+employeeId));
		employee.setProject(null);
		employee.setManager(null);	
		return employeeRepository.save(employee);		
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw ex;
        }
	}

	@Override
	public Request approveRequest(Long requestId) {
	try {
		Request request = requestRepository.findById(requestId)
				.orElseThrow(()-> new ResourceNotFoundException("No request found with Request Id: "+requestId));
		if(request.getRequestType()==RequestType.ASSIGN_EMPLOYEE) {
			int len = request.getEmployeeIds().size();
			for(int i=0; i<len; i++) {
				Long employeeId = request.getEmployeeIds().get(i);
				Employee employee = employeeRepository.findById(employeeId)
						.orElseThrow(()-> new ResourceNotFoundException("Employee not found with Id: "+ employeeId));	
				if(employee.getManager()==null && employee.getProject()==null) {
					this.assignProjectToEmployee(employee.getEmployeeId(), request.getProjectId());
				}else {
					request.setStatus(RequestStatus.REJECT);
					throw new EmployeeAlreadyAssignedException("Employee is already assigned to a manager or project");
				}
	            employeeRepository.save(employee);         
 }
			
		}else {
			int len = request.getEmployeeIds().size();
			for(int i=0; i<len; i++) {
				Long employeeId = request.getEmployeeIds().get(i);
				Employee employee = employeeRepository.findById(employeeId)
						.orElseThrow(()-> new ResourceNotFoundException("Employee not found with Id: "+ employeeId));
				employee.setManager(null);
				employee.setProject(null);
				employeeRepository.save(employee);
			}
		}
		request.setStatus(RequestStatus.APPROVED);
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
	public Request rejectRequest(Long requestId) {
	try {
		Request request = requestRepository.findById(requestId)
				.orElseThrow(()-> new ResourceNotFoundException("No request found with Request Id: "+requestId));
		request.setStatus(RequestStatus.REJECT);
		return requestRepository.save(request);		
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
	}

	@Override
	public void deleteEmployee(Long employeeId) {
	try {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(()->new ResourceNotFoundException("Employee not found with employee id: "+employeeId));
		employeeRepository.deleteById(employeeId);
		userRepository.deleteById(employee.getUser().getUserId());		
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
	}

	
	@Override
	public Employee getEmployeeById(Long employeeId) {
		Employee employee = employeeRepository.findByEmployeeId(employeeId)
				.orElseThrow(()-> new ResourceNotFoundException("Employee not found"));
		return employee;
	}
	
	@Override
	public Employee updateEmployee(EmployeeDTO employeeDTO) {
	try {
		Employee employee = employeeRepository.findById(employeeDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with employee id: "+employeeDTO.getEmployeeId()));	
		employee.setFullName(employeeDTO.getFullName());

        if (employeeDTO.getProjectId() != null) {
            Project project = projectRepository.findById(employeeDTO.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with project Id: "+employeeDTO.getProjectId()));
            employee.setProject(project);
            Manager manager = project.getManager();
            if(employeeDTO.getManagerId()!= manager.getManagerId()) {
            	throw new IllegalArgumentException("Manager Id does not match with project manager id");
            }
            employee.setManager(manager);
        } else {
            employee.setProject(null);
            employee.setManager(null);
        }

        if (employeeDTO.getManagerId() != null && employee.getProject()==null) {
            Manager manager = managerRepository.findById(employeeDTO.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("No manager found with manager Id: "+employeeDTO.getManagerId()));
            employee.setManager(manager);
        }

        employee.setSkills(employeeDTO.getSkills());

        employeeRepository.save(employee);
        return employee;	
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        }catch(IllegalArgumentException ex) {
        	logger.error("IllegalArgumentException: {}", ex.getMessage());
        	throw ex;
        }catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw ex;
        }
        
	}


	@Override
	public List<Request> getAllRequest() {
	try {
		List<Request> requestList = requestRepository.findAll();
		return requestList;
		} catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
	}



	@Override
	public Manager addmanager(ManagerDTO managerDTO) {
	try {
		Manager manager = new Manager();
		manager.setUser(userRepository.findById(managerDTO.getUserId())
				.orElseThrow(()->new ResourceNotFoundException("No user found with user id: "+managerDTO.getUserId())));
		return managerRepository.save(manager);
		}catch (ResourceNotFoundException ex) {
            logger.error("ResourceNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
	}



	@Override
	public List<Manager> getAllManager() {
		return managerService.getAllManagers();

	}
}
