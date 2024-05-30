package com.assignment.employeeManagement;

import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.repository.EmployeeRepository;
import com.assignment.employeeManagement.repository.ManagerRepository;
import com.assignment.employeeManagement.repository.ProjectRepository;
import com.assignment.employeeManagement.repository.UserLoginRepo;
import com.assignment.employeeManagement.service.EmployeeServiceIMPL;

public class EmployeeServiceTest {
	 @Mock
	    private EmployeeRepository employeeRepository;

	    @Mock
	    private UserLoginRepo userRepository;

	    @Mock
	    private ManagerRepository managerRepository;

	    @Mock
	    private ProjectRepository projectRepository;

	    @InjectMocks
	    private EmployeeServiceIMPL employeeService;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void testAddEmployee() {
	        EmployeeDTO employeeDTO = new EmployeeDTO();
	        employeeDTO.setUserId(1);
	        employeeDTO.setManagerId(1L);
	        employeeDTO.setProjectId(1L);
	        employeeDTO.setFullName("Rahul");

	        User user = new User();
	        Manager manager = new Manager();
	        Project project = new Project();

	        when(userRepository.findById(1)).thenReturn(Optional.of(user));
	        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
	        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
	        when(employeeRepository.save(any(Employee.class))).thenReturn(new Employee());

	        Employee result = employeeService.addEmployee(employeeDTO);

	        verify(employeeRepository, times(1)).save(any(Employee.class));
	        assertEquals(user, result.getUser());
	        assertEquals(manager, result.getManager());
	        assertEquals(project, result.getProject());
	    }

	    @Test
	    void testUpdateSkills() {
	        Principal principal = () -> "rahul@mail.com";
	        List<String> skills = Arrays.asList("Java", "Spring");
	        User user = new User();
	        Employee employee = new Employee();
	        employee.setSkills(new HashSet<>(Arrays.asList("Java")));

	        when(userRepository.findByUserEmail("rahul@mail.com")).thenReturn(user);
	        when(employeeRepository.findByUser(user)).thenReturn(employee);
	        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

	        Employee result = employeeService.updateSkills(principal, skills);

	        verify(employeeRepository, times(1)).save(any(Employee.class));
	        assertEquals(2, result.getSkills().size());
	    }
}
