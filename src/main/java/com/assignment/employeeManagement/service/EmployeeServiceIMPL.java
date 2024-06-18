package com.assignment.employeeManagement.service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.exception.ResourceNotFoundException;
import com.assignment.employeeManagement.repository.EmployeeRepository;
import com.assignment.employeeManagement.repository.ManagerRepository;
import com.assignment.employeeManagement.repository.ProjectRepository;
import com.assignment.employeeManagement.repository.UserLoginRepo;

@Service
public class EmployeeServiceIMPL implements EmployeeService {

	private static final Logger logger = LogManager.getLogger(EmployeeServiceIMPL.class);
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private UserLoginRepo userRepository;
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/*
	 * @Override public Employee addEmployee(EmployeeDTO employeeDTO) {
	 * 
	 * int userId = employeeDTO.getUserId(); User user =
	 * userRepository.findById(userId) .orElseThrow(() -> new
	 * ResourceNotFoundException("Invalid user ID"));
	 * 
	 * Long managerId = employeeDTO.getManagerId(); Manager manager = null; if
	 * (managerId != null) { manager = managerRepository.findById(managerId)
	 * .orElseThrow(() -> new ResourceNotFoundException("Invalid manager ID")); }
	 * 
	 * Long projectId = employeeDTO.getProjectId(); Project project = null; if
	 * (projectId != null) { project = projectRepository.findById(projectId)
	 * .orElseThrow(() -> new ResourceNotFoundException("Invalid project ID")); }
	 * 
	 * 
	 * Employee employee = new Employee( employeeDTO.getEmployeeId(), user,
	 * employeeDTO.getFullName(), project, manager, employeeDTO.getSkills() );
	 * 
	 * employeeRepository.save(employee); return employee; }
	 */

//	@Override
//	public Employee updateEmployee(Long employeeId, EmployeeDTO employeeDTO) {
//		
//		Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));
//
//       
//        employee.setFullName(employeeDTO.getFullName());
//
//        if (employeeDTO.getProjectId() != null) {
//            Project project = projectRepository.findById(employeeDTO.getProjectId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Invalid project ID"));
//            employee.setProject(project);
//        } else {
//            employee.setProject(null);
//        }
//
//        if (employeeDTO.getManagerId() != null) {
//            Manager manager = managerRepository.findById(employeeDTO.getManagerId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Invalid manager ID"));
//            employee.setManager(manager);
//        } else {
//            employee.setManager(null);
//        }
//
//        employee.setSkills(employeeDTO.getSkills());
//
//        employeeRepository.save(employee);
//        return employee;
//        
//	
//    }
//		Optional<Employee> employee = employeeRepository.findById(employeeId);
//        if (!employee.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//        User existingUser = employee.get();
//        existingUser.se;
//        existingUser.setUserEmail(employeeDTO.getUserEmail());
//        existingUser.setUserPassword(employeeDTO.getUserPassword());
//        existingUser.setUserRole(employeeDTO.getUserRole());
//        userLoginRepo.save(existingUser);
//        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
//    } catch (Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
//    }


//	@Override
//	public void deleteEmployee(Long employeeId) {
//		employeeRepository.deleteById(employeeId);
//	}



	@Override
	public List<Employee> getAllEmployees() {
		try {
			List<Employee> employeeList = employeeRepository.findAll();
			return employeeList;
		}catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw new RuntimeException("Internal Server Error");
        }	
	}

@Override
public Employee updateSkills(Principal principal, List<String> skills) {
	try {
		String email = principal.getName();
	    User user = userRepository.findByUserEmail(email);
	    if (user == null) {
	        throw new ResourceNotFoundException("User not found");
	    }

	    Employee employee = employeeRepository.findByUser(user);
	    if (employee == null) {
	        throw new ResourceNotFoundException("Employee not found");
	    }
	    
	    Set<String> skillSet = new HashSet<>(skills);
	    
	    employee.setSkills(skillSet);

	    return employeeRepository.save(employee);
		
	}catch (ResourceNotFoundException ex) {
        logger.error("ResourceNotFoundException: {}", ex.getMessage());
        throw ex;
    } catch (Exception ex) {
        logger.error("Exception: {}", ex.getMessage());
        throw new RuntimeException("Internal Server Error");
    }
	
}

@Override
public Employee getEmployeeInfo(Principal principal) {
	try {
		String email = principal.getName();
	    User user = userRepository.findByUserEmail(email);
	    if (user == null) {
	        throw new ResourceNotFoundException("User not found");
	    }

	    Employee employee = employeeRepository.findByUser(user);
	    if (employee == null) {
	        throw new ResourceNotFoundException("Employee not found");
	    }

	    return employee;
		
	}catch (ResourceNotFoundException ex) {
        logger.error("ResourceNotFoundException: {}", ex.getMessage());
        throw ex;
    } catch (Exception ex) {
        logger.error("Exception: {}", ex.getMessage());
        throw new RuntimeException("Internal Server Error");
    }
    
}

@Override
public User changePassword(Principal principal, String password) {
	try {
	String userEmail = principal.getName();
	User user = userRepository.findByUserEmail(userEmail);
	if (user == null) {
        throw new ResourceNotFoundException("User not found");
    }
	user.setUserPassword(this.passwordEncoder.encode(password));
	User user1 = userRepository.save(user);
	return user1;
	}catch(Exception e) {
		throw new InternalAuthenticationServiceException("Error occured in changing password");
	}
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
}




