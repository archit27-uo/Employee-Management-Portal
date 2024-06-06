package com.assignment.employeeManagement.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.assignment.employeeManagement.dto.ManagerInfoDTO;
import com.assignment.employeeManagement.dto.RequestDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.model.RequestStatus;
import com.assignment.employeeManagement.model.RequestType;
import com.assignment.employeeManagement.repository.EmployeeRepository;
import com.assignment.employeeManagement.repository.ManagerRepository;
import com.assignment.employeeManagement.repository.ProjectRepository;
import com.assignment.employeeManagement.repository.RequestRepository;
import com.assignment.employeeManagement.repository.UserLoginRepo;
import com.assignment.employeeManagement.service.ManagerServiceIMPL;

public class ManagerServiceIMPLTest {
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
    private Principal principal;
    
    @InjectMocks
    private ManagerServiceIMPL managerService;
    
    @InjectMocks
    private AdminServiceIMPL adminServiceImpl;
    
    private RequestDTO requestDTO;
    private Manager manager;
    private Request request;
    private User user;
    private Project project;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user = new User();
        user.setUserEmail("test@example.com");
        user.setUserName("John Doe");
        manager = new Manager();
        manager.setManagerId(1L);
        manager.setUser(user);
        
        requestDTO = new RequestDTO();
        requestDTO.setRequesterId(1L);
        requestDTO.setRequestType(RequestType.ASSIGN_EMPLOYEE);
        requestDTO.setProjectId(100L);
        requestDTO.setEmployeeIds(Arrays.asList(101L, 102L, 103L));
        requestDTO.setRequestDetails(null);

        request = new Request();
        request.setRequester(manager);
        request.setRequestType(RequestType.ASSIGN_EMPLOYEE);
        request.setProjectId(100L);
        request.setEmployeeIds(Arrays.asList(101L, 102L, 103L));
        request.setRequestDetails("Project ID: 100, Employee IDs: [101, 102, 103]");
        request.setStatus(RequestStatus.PENDING);
        
        project = new Project();
        project.setProjectId(100L);
        project.setManager(manager);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employeeList = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employeeList);

        List<Employee> result = managerService.getAllEmployees();

        assertEquals(employeeList, result);
    }

    @Test
    void testFilterEmployeesBySkills() {
        List<String> skills = Arrays.asList("Java", "Spring");
        List<Employee> employeeList = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findBySkillsIn(skills)).thenReturn(employeeList);

        List<Employee> result = managerService.filterEmployeesBySkills(skills);

        assertEquals(employeeList, result);
    }

//    @Test
//    void testRequestEmployeesForProject() {
//        String email = "manager@example.com";
//        Long projectId = 1L;
//        List<Long> employeeIds = Arrays.asList(1L, 2L);
//        Manager manager = new Manager();
//        Request request = new Request();
//
//        when(userRepository.findByUserEmail(email)).thenReturn(new User());
//        when(managerRepository.findByUser(any(User.class))).thenReturn(manager);
//        when(requestRepository.save(any(Request.class))).thenReturn(request);
//
//        Request result = managerService.requestEmployeesForProject(email, projectId, employeeIds);
//
//        verify(requestRepository, times(1)).save(any(Request.class));
//    }
    
    @Test
    public void testGetManagerInfo_Success() {
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(user);
        when(managerRepository.findByUser(user)).thenReturn(manager);
        when(projectRepository.findAllByManager(manager)).thenReturn(Collections.singletonList(project));
        when(employeeRepository.findAllByManager(manager)).thenReturn(Collections.emptyList());

        ManagerInfoDTO managerInfoDTO = managerService.getManagerInfo(principal);

        assertNotNull(managerInfoDTO);
        assertEquals(manager.getManagerId(), managerInfoDTO.getManagerId());
        assertEquals(user.getUserName(), managerInfoDTO.getFullName());
        assertEquals(1, managerInfoDTO.getProjectList().size());
        assertEquals(0, managerInfoDTO.getEmployeeList().size());
    }

    @Test
    public void testGetManagerInfo_UserNotFound() {
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            managerService.getManagerInfo(principal);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testGetManagerInfo_ManagerNotFound() {
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(user);
        when(managerRepository.findByUser(user)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            managerService.getManagerInfo(principal);
        });

        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    public void testGetAllEmployeeByProject() {
        Project project = new Project();
        project.setProjectId(1L);

        Employee employee1 = new Employee();
        Employee employee2 = new Employee();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.findAllByProject(project)).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> employees = managerService.getAllEmployeeByProject(1L);

        assertEquals(2, employees.size());
        verify(projectRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findAllByProject(project);
    }
    
    @Test
    public void testGetUnassignedEmployees() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();

        when(employeeRepository.findByProjectIsNull()).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> employees = managerService.getUnassignedEmployees();

        assertEquals(2, employees.size());
        verify(employeeRepository, times(1)).findByProjectIsNull();
    }
    
    @Test
    public void testGetAllProjects() {
        Project project1 = new Project();
        Project project2 = new Project();

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = managerService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetAllManagers() {
        Manager manager1 = new Manager();
        Manager manager2 = new Manager();

        when(managerRepository.findAll()).thenReturn(Arrays.asList(manager1, manager2));

        List<Manager> managers = managerService.getAllManagers();

        assertEquals(2, managers.size());
        verify(managerRepository, times(1)).findAll();
    }
    
    @Test
    public void testRequestEmployeesForProject() {
        when(managerRepository.findByManagerId(requestDTO.getRequesterId())).thenReturn(manager);
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        Request createdRequest = managerService.requestEmployeesForProject(requestDTO);

        assertEquals(request.getRequester(), createdRequest.getRequester());
        assertEquals(request.getRequestType(), createdRequest.getRequestType());
        assertEquals(request.getProjectId(), createdRequest.getProjectId());
        assertEquals(request.getEmployeeIds(), createdRequest.getEmployeeIds());
        assertEquals(request.getRequestDetails(), createdRequest.getRequestDetails());
        assertEquals(RequestStatus.PENDING, createdRequest.getStatus());
    }
}
