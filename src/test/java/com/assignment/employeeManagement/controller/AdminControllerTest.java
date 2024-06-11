package com.assignment.employeeManagement.controller;

import com.assignment.employeeManagement.dto.*;
import com.assignment.employeeManagement.entity.*;
import com.assignment.employeeManagement.payload.response.Response;
import com.assignment.employeeManagement.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private AdminService adminService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @Test
    public void testAddEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Employee employee = new Employee();
        when(adminService.addEmployee(any(EmployeeDTO.class))).thenReturn(employee);

        ResponseEntity<Employee> response = adminController.addEmployee(employeeDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testFetchAllEmployee() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = adminController.fetchAllEmployee();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
    }

    @Test
    public void testFetchEmployeeById() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        when(adminService.getEmployeeById(employeeId)).thenReturn(employee);

        ResponseEntity<Employee> response = adminController.fetchEmployeeById(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testDeleteEmployeeById() {
        Long employeeId = 1L;
        ResponseEntity<Response> response = adminController.deleteEmployeeById(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted", response.getBody().getMessage());
    }

    @Test
    public void testUpdateEmployee() {
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Employee employee = new Employee();
        when(adminService.updateEmployee( any(EmployeeDTO.class))).thenReturn(employee);

        ResponseEntity<Employee> response = adminController.updateEmployee( employeeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testAddProject() {
        ProjectDTO projectDTO = new ProjectDTO();
        Project project = new Project();
        when(adminService.addProject(any(ProjectDTO.class))).thenReturn(project);

        ResponseEntity<Project> response = adminController.addProject(projectDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(project, response.getBody());
    }

    @Test
    public void testGetAllProject() {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(adminService.getAllProjects()).thenReturn(projects);

        ResponseEntity<List<Project>> response = adminController.getAllProject();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projects, response.getBody());
    }

    @Test
    public void testAssignProject() {
        Long employeeId = 1L;
        Long projectId = 1L;
        Employee employee = new Employee();
        when(adminService.assignProjectToEmployee(employeeId, projectId)).thenReturn(employee);

        ResponseEntity<Employee> response = adminController.assignProject(employeeId, projectId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testUnassignProject() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        when(adminService.unassignProjectFromEmployee(employeeId)).thenReturn(employee);

        ResponseEntity<Employee> response = adminController.unassignProject(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testApproveProject() {
        Long requestId = 1L;
        Request request = new Request();
        when(adminService.approveRequest(requestId)).thenReturn(request);

        ResponseEntity<Request> response = adminController.approveProject(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request, response.getBody());
    }

    @Test
    public void testRejectRequest() {
        Long requestId = 1L;
        Request request = new Request();
        when(adminService.rejectRequest(requestId)).thenReturn(request);

        ResponseEntity<Request> response = adminController.rejectRequest(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request, response.getBody());
    }

    @Test
    public void testSaveUser() {
        UserAddDTO userAddDTO = new UserAddDTO();
        User user = new User();
        when(userService.addUser(any(UserAddDTO.class))).thenReturn(user);

        ResponseEntity<User> response = adminController.saveUser(userAddDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetAllRequest() {
        List<Request> requests = Arrays.asList(new Request(), new Request());
        when(adminService.getAllRequest()).thenReturn(requests);

        ResponseEntity<List<Request>> response = adminController.getAllRequest();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requests, response.getBody());
    }

    @Test
    public void testGetAllManager() {
        List<Manager> managers = Arrays.asList(new Manager(), new Manager());
        when(adminService.getAllManager()).thenReturn(managers);

        ResponseEntity<List<Manager>> response = adminController.getAllManager();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(managers, response.getBody());
    }

    @Test
    public void testAddManager() {
        ManagerDTO managerDTO = new ManagerDTO();
        Manager manager = new Manager();
        when(adminService.addmanager(any(ManagerDTO.class))).thenReturn(manager);

        ResponseEntity<Manager> response = adminController.addManager(managerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(manager, response.getBody());
    }
}
