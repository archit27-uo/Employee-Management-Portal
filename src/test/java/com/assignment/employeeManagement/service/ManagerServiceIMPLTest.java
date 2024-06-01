package com.assignment.employeeManagement.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.entity.User;
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

    @InjectMocks
    private ManagerServiceIMPL managerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

    @Test
    void testRequestEmployeesForProject() {
        String email = "manager@example.com";
        Long projectId = 1L;
        List<Long> employeeIds = Arrays.asList(1L, 2L);
        Manager manager = new Manager();
        Request request = new Request();

        when(userRepository.findByUserEmail(email)).thenReturn(new User());
        when(managerRepository.findByUser(any(User.class))).thenReturn(manager);
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        Request result = managerService.requestEmployeesForProject(email, projectId, employeeIds);

        verify(requestRepository, times(1)).save(any(Request.class));
    }
    
    @Test
    public void testGetManagerInfo() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("manager@example.com");

        User user = new User();
        user.setUserEmail("manager@example.com");

        Manager manager = new Manager();

        when(userRepository.findByUserEmail("manager@example.com")).thenReturn(user);
        when(managerRepository.findByUser(user)).thenReturn(manager);

        Manager foundManager = managerService.getManagerInfo(principal);

        assertEquals(manager, foundManager);
    }

    @Test
    public void testGetAllEmployeeByProject() {
        Project project = new Project();
        project.setProjectId(1L);

        Employee employee1 = new Employee();
        Employee employee2 = new Employee();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.findByProject(project)).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> employees = managerService.getAllEmployeeByProject(1L);

        assertEquals(2, employees.size());
        verify(projectRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findByProject(project);
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
}
