package com.assignment.employeeManagement.service;

import com.assignment.employeeManagement.dto.ManagerInfoDTO;
import com.assignment.employeeManagement.dto.RequestDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.exception.EmployeeAlreadyAssignedException;
import com.assignment.employeeManagement.exception.ResourceNotFoundException;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ManagerServiceIMPLTest {

    @Mock
    private EmployeeService employeeService;

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

    @Test
    void getAllManagers_Success() {
        Manager manager1 = new Manager();
        Manager manager2 = new Manager();
        List<Manager> managers = Arrays.asList(manager1, manager2);

        when(managerRepository.findAll()).thenReturn(managers);

        List<Manager> result = managerService.getAllManagers();

        assertEquals(2, result.size());
        verify(managerRepository, times(1)).findAll();
    }

    @Test
    void getAllManagers_Exception() {
        when(managerRepository.findAll()).thenThrow(new RuntimeException("Internal Server Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> managerService.getAllManagers());

        assertEquals("Internal Server Error", exception.getMessage());
        verify(managerRepository, times(1)).findAll();
    }
    
    @Test
    void getAllProjects_Success() {
        Project project1 = new Project();
        Project project2 = new Project();
        List<Project> projects = Arrays.asList(project1, project2);

        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = managerService.getAllProjects();

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getAllProjects_Exception() {
        when(projectRepository.findAll()).thenThrow(new RuntimeException("Internal Server Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> managerService.getAllProjects());

        assertEquals("Internal Server Error", exception.getMessage());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void filterEmployeesBySkills_Success() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeRepository.findBySkillsIn(anyList())).thenReturn(employees);

        List<Employee> result = managerService.filterEmployeesBySkills(Arrays.asList("Java", "Spring"));

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findBySkillsIn(anyList());
    }

    @Test
    void filterEmployeesBySkills_Exception() {
        when(employeeRepository.findBySkillsIn(anyList())).thenThrow(new RuntimeException("Internal Server Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> managerService.filterEmployeesBySkills(Arrays.asList("Java", "Spring")));

        assertEquals("Internal Server Error", exception.getMessage());
        verify(employeeRepository, times(1)).findBySkillsIn(anyList());
    }

    @Test
    void getUnassignedEmployees_Success() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeRepository.findByProjectIsNull()).thenReturn(employees);

        List<Employee> result = managerService.getUnassignedEmployees();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findByProjectIsNull();
    }

    @Test
    void getUnassignedEmployees_Exception() {
        when(employeeRepository.findByProjectIsNull()).thenThrow(new RuntimeException("Internal Server Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> managerService.getUnassignedEmployees());

        assertEquals("Internal Server Error", exception.getMessage());
        verify(employeeRepository, times(1)).findByProjectIsNull();
    }

    @Test
    void requestEmployeesForProject_Success() {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setRequesterId(1L);
        requestDTO.setProjectId(1L);
        requestDTO.setRequestType(RequestType.ASSIGN_EMPLOYEE);
        requestDTO.setEmployeeIds(Arrays.asList(1L, 2L));

        Manager manager = new Manager();
        manager.setManagerId(1L);
        Project project = new Project();
        project.setManager(manager);

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(new Employee()));

        Request request = new Request();
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        Request result = managerService.requestEmployeesForProject(requestDTO);

        assertNotNull(result);
        verify(managerRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).findById(1L);
        verify(employeeRepository, times(2)).findById(anyLong());
        verify(requestRepository, times(1)).save(any(Request.class));
    }

	/*
	 * @Test void requestEmployeesForProject_EmployeeAlreadyAssignedException() {
	 * RequestDTO requestDTO = new RequestDTO(); requestDTO.setRequesterId(1L);
	 * requestDTO.setProjectId(1L);
	 * requestDTO.setRequestType(RequestType.ASSIGN_EMPLOYEE);
	 * requestDTO.setEmployeeIds(Arrays.asList(1L, 2L));
	 * 
	 * Manager manager = new Manager(); manager.setManagerId(1L); Project project =
	 * new Project(); project.setManager(manager);
	 * 
	 * Employee employee = new Employee(); employee.setProject(new Project());
	 * 
	 * when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
	 * when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
	 * when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee)
	 * );
	 * 
	 * Exception exception = assertThrows(EmployeeAlreadyAssignedException.class, ()
	 * -> managerService.requestEmployeesForProject(requestDTO));
	 * 
	 * assertEquals("Employee is already assigned to a project, employee id: 1",
	 * exception.getMessage()); verify(managerRepository, times(1)).findById(1L);
	 * verify(projectRepository, times(1)).findById(1L); verify(employeeRepository,
	 * times(1)).findById(anyLong()); }
	 */

    @Test
    void requestEmployeesForProject_Exception() {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setRequesterId(1L);
        requestDTO.setProjectId(1L);
        requestDTO.setRequestType(RequestType.ASSIGN_EMPLOYEE);
        requestDTO.setEmployeeIds(Arrays.asList(1L, 2L));

        when(managerRepository.findById(1L)).thenThrow(new RuntimeException("Internal Server Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> managerService.requestEmployeesForProject(requestDTO));

        assertEquals("Internal Server Error", exception.getMessage());
        verify(managerRepository, times(1)).findById(1L);
    }

    @Test
    void getManagerInfo_Success() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("manager@example.com");

        User user = new User();
        user.setUserEmail("manager@example.com");

        Manager manager = new Manager();
        manager.setManagerId(1L);
        manager.setUser(user);

        when(userRepository.findByUserEmail("manager@example.com")).thenReturn(user);
        when(managerRepository.findByUser(user)).thenReturn(Optional.of(manager));
        when(projectRepository.findAllByManager(manager)).thenReturn(new ArrayList<>());
        when(employeeRepository.findAllByManager(manager)).thenReturn(Optional.of(new ArrayList<>()));

        ManagerInfoDTO result = managerService.getManagerInfo(principal);

        assertEquals(1L, result.getManagerId());
        verify(userRepository, times(1)).findByUserEmail("manager@example.com");
        verify(managerRepository, times(1)).findByUser(user);
    }

    @Test
    void getManagerInfo_Exception() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("manager@example.com");

        when(userRepository.findByUserEmail("manager@example.com")).thenThrow(new RuntimeException("Internal Server Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> managerService.getManagerInfo(principal));

        assertEquals("Internal Server Error", exception.getMessage());
        verify(userRepository, times(1)).findByUserEmail("manager@example.com");
    }

    @Test
    void getAllEmployeeByProject_Success() {
        Project project = new Project();
        project.setProjectId(1L);
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.findAllByProject(project)).thenReturn(employees);

        List<Employee> result = managerService.getAllEmployeeByProject(1L);

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findAllByProject(project);
    }

    @Test
    void getAllEmployeeByProject_Exception() {
        when(projectRepository.findById(1L)).thenThrow(new RuntimeException("Internal Server Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> managerService.getAllEmployeeByProject(1L));

        assertEquals("Internal Server Error", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void getAllRequestByManager_Success() {
        Manager manager = new Manager();
        manager.setManagerId(1L);
        List<Request> requests = Arrays.asList(new Request(), new Request());

        when(managerRepository.findByManagerId(1L)).thenReturn(manager);
        when(requestRepository.findAllByRequester(manager)).thenReturn(requests);

        List<Request> result = managerService.getAllRequestByManager(1L);

        assertEquals(2, result.size());
        verify(managerRepository, times(1)).findByManagerId(1L);
        verify(requestRepository, times(1)).findAllByRequester(manager);
    }

    @Test
    void getAllRequestByManager_Exception() {
        when(managerRepository.findByManagerId(1L)).thenThrow(new RuntimeException("Internal Server Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> managerService.getAllRequestByManager(1L));

        assertEquals("Internal Server Error", exception.getMessage());
        verify(managerRepository, times(1)).findByManagerId(1L);
    }

    @Test
    void getAllEmployeeByManager_Success() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("manager@example.com");

        User user = new User();
        user.setUserEmail("manager@example.com");

        Manager manager = new Manager();
        manager.setManagerId(1L);
        manager.setUser(user);

        List<Employee> employees = Arrays.asList(new Employee(), new Employee());

        when(userRepository.findByUserEmail("manager@example.com")).thenReturn(user);
        when(managerRepository.findByUser(user)).thenReturn(Optional.of(manager));
        when(employeeRepository.findAllByManager(manager)).thenReturn(Optional.of(employees));

        List<Employee> result = managerService.getAllEmployeeByManager(principal);

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findByUserEmail("manager@example.com");
        verify(managerRepository, times(1)).findByUser(user);
        verify(employeeRepository, times(1)).findAllByManager(manager);
    }

    @Test
    void getAllEmployeeByManager_Exception() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("manager@example.com");

        when(userRepository.findByUserEmail("manager@example.com")).thenThrow(new RuntimeException("Internal Server Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> managerService.getAllEmployeeByManager(principal));

        assertEquals("Internal Server Error", exception.getMessage());
        verify(userRepository, times(1)).findByUserEmail("manager@example.com");
    }

}
