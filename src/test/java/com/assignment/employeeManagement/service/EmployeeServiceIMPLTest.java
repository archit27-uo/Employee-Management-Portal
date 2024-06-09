package com.assignment.employeeManagement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

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

@SpringBootTest
public class EmployeeServiceIMPLTest {

    @InjectMocks
    private EmployeeServiceIMPL employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserLoginRepo userRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private Principal principal;

    private User user;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user = new User();
        user.setUserId(1);
        user.setUserEmail("test@example.com");

        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setUser(user);
        employee.setFullName("John Doe");
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee);

        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(employees, result);
    }

    @Test
    public void testGetAllEmployees_Exception() {
        when(employeeRepository.findAll()).thenThrow(new RuntimeException("Internal Server Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeService.getAllEmployees();
        });

        assertEquals("Internal Server Error", thrown.getMessage());
    }

    @Test
    public void testUpdateSkills() {
        List<String> skills = Arrays.asList("Java", "Spring");

        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(user);
        when(employeeRepository.findByUser(user)).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.updateSkills(principal, skills);

        assertEquals(new HashSet<>(skills), result.getSkills());
    }

    @Test
    public void testUpdateSkills_UserNotFound() {
        List<String> skills = Arrays.asList("Java", "Spring");

        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateSkills(principal, skills);
        });

        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    public void testUpdateSkills_EmployeeNotFound() {
        List<String> skills = Arrays.asList("Java", "Spring");

        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(user);
        when(employeeRepository.findByUser(user)).thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateSkills(principal, skills);
        });

        assertEquals("Employee not found", thrown.getMessage());
    }

    @Test
    public void testGetEmployeeInfo() {
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(user);
        when(employeeRepository.findByUser(user)).thenReturn(employee);

        Employee result = employeeService.getEmployeeInfo(principal);

        assertEquals(employee, result);
    }

    @Test
    public void testGetEmployeeInfo_UserNotFound() {
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeInfo(principal);
        });

        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    public void testGetEmployeeInfo_EmployeeNotFound() {
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(user);
        when(employeeRepository.findByUser(user)).thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeInfo(principal);
        });

        assertEquals("Employee not found", thrown.getMessage());
    }
}
