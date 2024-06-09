package com.assignment.employeeManagement.controller;

import com.assignment.employeeManagement.dto.ManagerInfoDTO;
import com.assignment.employeeManagement.dto.RequestDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.service.ManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManagerControllerTest {

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private ManagerController managerController;

    @Test
    public void testGetAllEmployee() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(managerService.getAllEmployees()).thenReturn(employees);

        List<Employee> response = managerController.getAllEmployee();

        assertEquals(employees, response);
    }

    @Test
    public void testGetAllManager() {
        List<Manager> managers = Arrays.asList(new Manager(), new Manager());
        when(managerService.getAllManagers()).thenReturn(managers);

        ResponseEntity<List<Manager>> response = managerController.getAllManager();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(managers, response.getBody());
    }

    @Test
    public void testGetAllProject() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(managerService.getAllProjects()).thenReturn(projects);

        ResponseEntity<List<Project>> response = managerController.getAllProject();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projects, response.getBody());
    }

    @Test
    public void testGetUnassignedEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(managerService.getUnassignedEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = managerController.getUnassignedEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
    }

    @Test
    public void testFilterAllEmployeesBySkills() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        List<String> skills = Arrays.asList("Java", "Spring");
        when(managerService.filterEmployeesBySkills(skills)).thenReturn(employees);

        ResponseEntity<List<Employee>> response = managerController.filterAllEmployeesBySkills(skills);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
    }

    @Test
    public void testRequestEmployeesForProject() {
        RequestDTO requestDTO = new RequestDTO();
        Request request = new Request();
        when(managerService.requestEmployeesForProject(any(RequestDTO.class))).thenReturn(request);

        ResponseEntity<Request> response = managerController.requestEmployeesForProject(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request, response.getBody());
    }

    @Test
    public void testGetManagerInfo() {
        Principal principal = () -> "username";
        ManagerInfoDTO managerInfoDTO = new ManagerInfoDTO();
        when(managerService.getManagerInfo(any(Principal.class))).thenReturn(managerInfoDTO);

        ResponseEntity<ManagerInfoDTO> response = managerController.getManagerInfo(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(managerInfoDTO, response.getBody());
    }

    @Test
    public void testGetEmployeeListByProjectId() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(managerService.getAllEmployeeByProject(anyLong())).thenReturn(employees);

        ResponseEntity<List<Employee>> response = managerController.getEmployeeListByProjectId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
    }

    @Test
    public void testGetRequestByManagerId() {
        List<Request> requests = Arrays.asList(new Request(), new Request());
        when(managerService.getAllRequestByManager(anyLong())).thenReturn(requests);

        ResponseEntity<List<Request>> response = managerController.getRequestByManagerId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requests, response.getBody());
    }

    @Test
    public void testGetMyEmployee() {
        Principal principal = () -> "username";
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(managerService.getAllEmployeeByManager(any(Principal.class))).thenReturn(employees);

        ResponseEntity<List<Employee>> response = managerController.getMyEmployee(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
    }
}
