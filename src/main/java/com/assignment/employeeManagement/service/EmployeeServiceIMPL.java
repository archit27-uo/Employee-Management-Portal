package com.assignment.employeeManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.repository.EmployeeRepository;
import com.assignment.employeeManagement.repository.ManagerRepository;
import com.assignment.employeeManagement.repository.ProjectRepository;
import com.assignment.employeeManagement.repository.UserLoginRepo;

@Service
public class EmployeeServiceIMPL implements EmployeeService {

	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private UserLoginRepo userRepository;
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
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


	@Override
	public void deleteEmployee(Long employeeId) {
		employeeRepository.deleteById(employeeId);
		
	}

	@Override
	public Optional<Employee> getEmployeeById(Long employeeId) {
		Optional<Employee> employee = employeeRepository.findByEmployeeId(employeeId);
		return employee;
	}

	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}



}
