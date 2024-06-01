package com.assignment.employeeManagement.service;

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
import static org.junit.Assert.assertTrue;
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

public class EmployeeServiceIMPLTest {
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
	    @Test
	    public void testUpdateEmployee() {
	        EmployeeDTO employeeDTO = new EmployeeDTO();
	        employeeDTO.setUserId(1);
	        employeeDTO.setFullName("John Doe Updated");
	        employeeDTO.setProjectId(1L);
	        employeeDTO.setManagerId(1L);
	        employeeDTO.setSkills(new HashSet<>(Arrays.asList("Java", "Spring")));

	        Employee employee = new Employee();
	        User user = new User();
	        Project project = new Project();
	        Manager manager = new Manager();

	        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
	        when(userRepository.findById(1)).thenReturn(Optional.of(user));
	        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
	        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
	        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

	        Employee updatedEmployee = employeeService.updateEmployee(1L, employeeDTO);

	        assertEquals("John Doe Updated", updatedEmployee.getFullName());
	        assertEquals(user, updatedEmployee.getUser());
	        assertEquals(project, updatedEmployee.getProject());
	        assertEquals(manager, updatedEmployee.getManager());
	        assertTrue(updatedEmployee.getSkills().contains("Java"));
	        assertTrue(updatedEmployee.getSkills().contains("Spring"));

	        verify(employeeRepository, times(1)).save(employee);
	    }
	    
	    @Test
	    public void testGetEmployeeInfo() {
	        Principal principal = mock(Principal.class);
	        when(principal.getName()).thenReturn("user@example.com");

	        User user = new User();
	        user.setUserEmail("user@example.com");

	        Employee employee = new Employee();

	        when(userRepository.findByUserEmail("user@example.com")).thenReturn(user);
	        when(employeeRepository.findByUser(user)).thenReturn(employee);

	        Employee foundEmployee = employeeService.getEmployeeInfo(principal);

	        assertEquals(employee, foundEmployee);
	    }
	    
	    @Test
	    public void testGetEmployeeById() {
	        Employee employee = new Employee();
	        when(employeeRepository.findByEmployeeId(1L)).thenReturn(Optional.of(employee));

	        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

	        assertTrue(foundEmployee.isPresent());
	        assertEquals(employee, foundEmployee.get());
	        verify(employeeRepository, times(1)).findByEmployeeId(1L);
	    }

	    @Test
	    public void testGetAllEmployees() {
	        Employee employee1 = new Employee();
	        Employee employee2 = new Employee();

	        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

	        List<Employee> employees = employeeService.getAllEmployees();

	        assertEquals(2, employees.size());
	        verify(employeeRepository, times(1)).findAll();
	    }

	    @Test
	    public void testDeleteEmployee() {
	        doNothing().when(employeeRepository).deleteById(1L);

	        employeeService.deleteEmployee(1L);

	        verify(employeeRepository, times(1)).deleteById(1L);
	    }
}
