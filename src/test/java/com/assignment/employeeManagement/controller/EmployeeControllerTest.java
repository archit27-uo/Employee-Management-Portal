package com.assignment.employeeManagement.controller;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.dto.UserLoginDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.payload.response.Response;
import com.assignment.employeeManagement.service.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

	
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testFetchAllEmployee() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.fetchAllEmployee();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
    }

    @Test
    public void testUpdateSkills() {
        Principal principal = () -> "username";
        List<String> skills = Arrays.asList("Java", "Spring");
        Employee employee = new Employee();
        when(employeeService.updateSkills(any(Principal.class), any(List.class))).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.updateSkills(principal, skills);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testGetInfo() {
        Principal principal = () -> "username";
        Employee employee = new Employee();
        when(employeeService.getEmployeeInfo(any(Principal.class))).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.getInfo(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    void changePassword_ReturnsSuccessResponse() {
        // Arrange
    	Principal principal = () -> "user";
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserPassword("newPassword");
        
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUserEmail("user");
        mockUser.setUserPassword("newPassword");
        
        // Mock the behavior to return a User object
        when(employeeService.changePassword(principal, "newPassword")).thenReturn(mockUser);

        // Act
        ResponseEntity<Response> responseEntity = employeeController.changePassword(principal, userLoginDTO);

        // Assert
        //assertEquals(200, responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("Changed password successfully", responseEntity.getBody().getMessage());
        verify(employeeService).changePassword(principal, "newPassword");
    }
}
