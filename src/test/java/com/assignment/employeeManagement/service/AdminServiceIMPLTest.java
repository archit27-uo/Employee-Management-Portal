package com.assignment.employeeManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.dto.ManagerDTO;
import com.assignment.employeeManagement.dto.ProjectDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AdminServiceIMPLTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserLoginRepo userRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private AdminServiceIMPL adminService;

    private User user;
    private Employee employee;
    private Manager manager;
    private Project project;
    private Request request;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(1);

        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setUser(user);

        manager = new Manager();
        manager.setManagerId(1L);
        manager.setUser(user);

        project = new Project();
        project.setProjectId(1L);
        project.setProjectName("Test Project");
        project.setManager(manager);

        request = new Request();
        request.setRequestId(1L);
        request.setRequestType(RequestType.ASSIGN_EMPLOYEE);
        request.setEmployeeIds(Collections.singletonList(employee.getEmployeeId()));
        request.setProjectId(project.getProjectId());
    }

    @Test
    public void testAddEmployee_Success() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserId(user.getUserId());
        employeeDTO.setEmployeeId(employee.getEmployeeId());

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = adminService.addEmployee(employeeDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(user.getUserId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testAddEmployee_UserNotFound() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserId(user.getUserId());

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.addEmployee(employeeDTO));

        verify(userRepository, times(1)).findById(user.getUserId());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testAddEmployee_ProjectNotFound() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserId(user.getUserId());
        employeeDTO.setProjectId(project.getProjectId());

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.addEmployee(employeeDTO));

        verify(userRepository, times(1)).findById(user.getUserId());
        verify(projectRepository, times(1)).findById(project.getProjectId());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testAddEmployee_ManagerMismatch() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserId(user.getUserId());
        employeeDTO.setProjectId(project.getProjectId());
        employeeDTO.setManagerId(2L);

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));

        assertThrows(IllegalArgumentException.class, () -> adminService.addEmployee(employeeDTO));

        verify(userRepository, times(1)).findById(user.getUserId());
        verify(projectRepository, times(1)).findById(project.getProjectId());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testAddProject_Success() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = adminService.addProject(projectDTO);

        assertNotNull(result);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    public void testAddProject_ManagerNotFound() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setManagerId(manager.getManagerId());

        when(managerRepository.findById(manager.getManagerId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.addProject(projectDTO));

        verify(managerRepository, times(1)).findById(manager.getManagerId());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee);

        when(employeeService.getAllEmployees()).thenReturn(employees);

        List<Employee> result = adminService.getAllEmployees();

        assertEquals(1, result.size());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    public void testGetAllProjects() {
        List<Project> projects = Arrays.asList(project);

        when(managerService.getAllProjects()).thenReturn(projects);

        List<Project> result = adminService.getAllProjects();

        assertEquals(1, result.size());
        verify(managerService, times(1)).getAllProjects();
    }

    @Test
    public void testAssignProjectToEmployee_Success() {
        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));
        when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = adminService.assignProjectToEmployee(employee.getEmployeeId(), project.getProjectId());

        assertNotNull(result);
        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(projectRepository, times(1)).findById(project.getProjectId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testAssignProjectToEmployee_EmployeeNotFound() {
        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.assignProjectToEmployee(employee.getEmployeeId(), project.getProjectId()));

        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(projectRepository, never()).findById(project.getProjectId());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testAssignProjectToEmployee_ProjectNotFound() {
        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));
        when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.assignProjectToEmployee(employee.getEmployeeId(), project.getProjectId()));

        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(projectRepository, times(1)).findById(project.getProjectId());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testAssignProjectToEmployee_EmployeeAlreadyAssigned() {
        employee.setProject(project);

        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));
        when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));

        assertThrows(EmployeeAlreadyAssignedException.class, () -> adminService.assignProjectToEmployee(employee.getEmployeeId(), project.getProjectId()));

        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(projectRepository, times(1)).findById(project.getProjectId());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testUnassignProjectFromEmployee_Success() {
        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = adminService.unassignProjectFromEmployee(employee.getEmployeeId());

        assertNotNull(result);
        assertNull(result.getProject());
        assertNull(result.getManager());
        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUnassignProjectFromEmployee_EmployeeNotFound() {
        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.unassignProjectFromEmployee(employee.getEmployeeId()));

        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

//    @Test
//    public void testApproveRequest_Success() {
//        when(requestRepository.findById(request.getRequestId())).thenReturn(Optional.of(request));
//        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));
//        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
//        when(requestRepository.save(any(Request.class))).thenReturn(request);
//
//        Request result = adminService.approveRequest(request.getRequestId());
//
//        assertNotNull(result);
//        assertEquals(RequestStatus.APPROVED, result.getStatus());
//        verify(requestRepository, times(1)).findById(request.getRequestId());
//        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
//        verify(employeeRepository, times(1)).save(any(Employee.class));
//        verify(requestRepository, times(1)).save(any(Request.class));
//    }

    @Test
    public void testApproveRequest_RequestNotFound() {
        when(requestRepository.findById(request.getRequestId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.approveRequest(request.getRequestId()));

        verify(requestRepository, times(1)).findById(request.getRequestId());
        verify(employeeRepository, never()).findById(employee.getEmployeeId());
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    public void testApproveRequest_EmployeeNotFound() {
        when(requestRepository.findById(request.getRequestId())).thenReturn(Optional.of(request));
        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.approveRequest(request.getRequestId()));

        verify(requestRepository, times(1)).findById(request.getRequestId());
        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    public void testApproveRequest_EmployeeAlreadyAssigned() {
        employee.setProject(project);
        when(requestRepository.findById(request.getRequestId())).thenReturn(Optional.of(request));
        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));

        assertThrows(EmployeeAlreadyAssignedException.class, () -> adminService.approveRequest(request.getRequestId()));

        verify(requestRepository, times(1)).findById(request.getRequestId());
        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    public void testRejectRequest_Success() {
        when(requestRepository.findById(request.getRequestId())).thenReturn(Optional.of(request));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        Request result = adminService.rejectRequest(request.getRequestId());

        assertNotNull(result);
        assertEquals(RequestStatus.REJECT, result.getStatus());
        verify(requestRepository, times(1)).findById(request.getRequestId());
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    public void testRejectRequest_RequestNotFound() {
        when(requestRepository.findById(request.getRequestId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.rejectRequest(request.getRequestId()));

        verify(requestRepository, times(1)).findById(request.getRequestId());
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    public void testDeleteEmployee_Success() {
        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));

        doNothing().when(employeeRepository).deleteById(employee.getEmployeeId());
        doNothing().when(userRepository).deleteById(user.getUserId());

        adminService.deleteEmployee(employee.getEmployeeId());

        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(employeeRepository, times(1)).deleteById(employee.getEmployeeId());
        verify(userRepository, times(1)).deleteById(user.getUserId());
    }

    @Test
    public void testDeleteEmployee_EmployeeNotFound() {
        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.deleteEmployee(employee.getEmployeeId()));

        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(employeeRepository, never()).deleteById(employee.getEmployeeId());
        verify(userRepository, never()).deleteById(user.getUserId());
    }

    @Test
    public void testGetEmployeeById_Success() {
        when(employeeRepository.findByEmployeeId(employee.getEmployeeId())).thenReturn(Optional.of(employee));

        Employee result = adminService.getEmployeeById(employee.getEmployeeId());

        assertNotNull(result);
        verify(employeeRepository, times(1)).findByEmployeeId(employee.getEmployeeId());
    }

    @Test
    public void testGetEmployeeById_EmployeeNotFound() {
        when(employeeRepository.findByEmployeeId(employee.getEmployeeId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getEmployeeById(employee.getEmployeeId()));

        verify(employeeRepository, times(1)).findByEmployeeId(employee.getEmployeeId());
    }

    @Test
    public void testUpdateEmployee_Success() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        employeeDTO.setFullName("Updated Name");

        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = adminService.updateEmployee(employeeDTO);

        assertNotNull(result);
        assertEquals("Updated Name", result.getFullName());
        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee_EmployeeNotFound() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmployeeId());

        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.updateEmployee(employeeDTO));

        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee_ProjectNotFound() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        employeeDTO.setProjectId(project.getProjectId());

        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));
        when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.updateEmployee(employeeDTO));

        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(projectRepository, times(1)).findById(project.getProjectId());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee_ManagerMismatch() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        employeeDTO.setProjectId(project.getProjectId());
        employeeDTO.setManagerId(2L);

        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));
        when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));

        assertThrows(IllegalArgumentException.class, () -> adminService.updateEmployee(employeeDTO));

        verify(employeeRepository, times(1)).findById(employee.getEmployeeId());
        verify(projectRepository, times(1)).findById(project.getProjectId());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testGetAllRequest() {
        List<Request> requests = Arrays.asList(request);

        when(requestRepository.findAll()).thenReturn(requests);

        List<Request> result = adminService.getAllRequest();

        assertEquals(1, result.size());
        verify(requestRepository, times(1)).findAll();
    }

    @Test
    public void testAddManager_Success() {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setUserId(user.getUserId());

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);

        Manager result = adminService.addmanager(managerDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(user.getUserId());
        verify(managerRepository, times(1)).save(any(Manager.class));
    }

    @Test
    public void testAddManager_UserNotFound() {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setUserId(user.getUserId());

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.addmanager(managerDTO));

        verify(userRepository, times(1)).findById(user.getUserId());
        verify(managerRepository, never()).save(any(Manager.class));
    }

    @Test
    public void testGetAllManager() {
        List<Manager> managers = Arrays.asList(manager);

        when(managerService.getAllManagers()).thenReturn(managers);

        List<Manager> result = adminService.getAllManager();

        assertEquals(1, result.size());
        verify(managerService, times(1)).getAllManagers();
    }
}
