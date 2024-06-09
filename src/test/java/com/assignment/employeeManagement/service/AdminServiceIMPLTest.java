package com.assignment.employeeManagement.service;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.dto.ManagerDTO;
import com.assignment.employeeManagement.dto.ProjectDTO;
import com.assignment.employeeManagement.entity.*;
import com.assignment.employeeManagement.exception.EmployeeAlreadyAssignedException;
import com.assignment.employeeManagement.exception.ResourceNotFoundException;
import com.assignment.employeeManagement.model.RequestStatus;
import com.assignment.employeeManagement.model.RequestType;
import com.assignment.employeeManagement.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AdminServiceIMPLTest {

    @InjectMocks
    private AdminServiceIMPL adminService;

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

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addEmployee_Success() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserId(1);
        employeeDTO.setManagerId(2L);
        employeeDTO.setProjectId(3L);
        User user = new User();
        user.setUserId(1);
        Manager manager = new Manager();
        manager.setManagerId(2L);
        Project project = new Project();
        project.setProjectId(3L);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(managerRepository.findById(2L)).thenReturn(Optional.of(manager));
        when(projectRepository.findById(3L)).thenReturn(Optional.of(project));

        Employee employee = new Employee();
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = adminService.addEmployee(employeeDTO);

        assertNotNull(result);
        verify(userRepository).findById(1);
        verify(managerRepository).findById(2L);
        verify(projectRepository).findById(3L);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void addEmployee_ResourceNotFoundException() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.addEmployee(employeeDTO);
        });

        assertEquals("No user found with user id: 1", exception.getMessage());
        verify(userRepository).findById(1);
    }

    @Test
    void addEmployee_Exception() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserId(1);

        when(userRepository.findById(1)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.addEmployee(employeeDTO);
        });

        assertEquals("Internal Server Error", exception.getMessage());
        verify(userRepository).findById(1);
    }

    @Test
    void addProject_Success() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setManagerId(1L);
        Manager manager = new Manager();
        manager.setManagerId(1L);
        
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

        Project project = new Project();
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = adminService.addProject(projectDTO);

        assertNotNull(result);
        verify(managerRepository).findById(1L);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void addProject_ResourceNotFoundException() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setManagerId(1L);

        when(managerRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.addProject(projectDTO);
        });

        assertEquals("No Manager found with manager Id: 1", exception.getMessage());
        verify(managerRepository).findById(1L);
    }

    @Test
    void addProject_Exception() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setManagerId(1L);

        when(managerRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.addProject(projectDTO);
        });

        assertEquals("Internal Server Error", exception.getMessage());
        verify(managerRepository).findById(1L);
    }

    @Test
    void getAllEmployees_Success() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());

        when(employeeService.getAllEmployees()).thenReturn(employees);

        List<Employee> result = adminService.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeService).getAllEmployees();
    }

    @Test
    void getAllProjects_Success() {
        List<Project> projects = Arrays.asList(new Project(), new Project());

        when(managerService.getAllProjects()).thenReturn(projects);

        List<Project> result = adminService.getAllProjects();

        assertEquals(2, result.size());
        verify(managerService).getAllProjects();
    }

    
    @Test
    void assignProjectToEmployee_Success() {
        Long employeeId = 1L;
        Long projectId = 2L;
        Employee employee = new Employee();
        Project project = new Project();
        
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = adminService.assignProjectToEmployee(employeeId, projectId);

        assertNotNull(result);
        verify(employeeRepository).findById(employeeId);
        verify(projectRepository).findById(projectId);
        verify(employeeRepository).save(employee);
    }

    @Test
    void assignProjectToEmployee_ResourceNotFoundException() {
        Long employeeId = 1L;
        Long projectId = 2L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.assignProjectToEmployee(employeeId, projectId);
        });

        assertEquals("No employee found with Employee ID: 1", exception.getMessage());
        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void assignProjectToEmployee_Exception() {
        Long employeeId = 1L;
        Long projectId = 2L;

        when(employeeRepository.findById(employeeId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.assignProjectToEmployee(employeeId, projectId);
        });

        assertEquals("Internal Server Error", exception.getMessage());
        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void unassignProjectFromEmployee_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setProject(new Project());
        employee.setManager(new Manager());

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = adminService.unassignProjectFromEmployee(employeeId);

        assertNotNull(result);
        assertNull(result.getProject());
        assertNull(result.getManager());
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).save(employee);
    }

    @Test
    void unassignProjectFromEmployee_ResourceNotFoundException() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.unassignProjectFromEmployee(employeeId);
        });

        assertEquals("No Employee found with Employee ID: 1", exception.getMessage());
        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void unassignProjectFromEmployee_Exception() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.unassignProjectFromEmployee(employeeId);
        });

        assertEquals("Internal Server Error", exception.getMessage());
        verify(employeeRepository).findById(employeeId);
    }

	/*
	 * @Test void approveRequest_Success() { Long requestId = 1L; Request request =
	 * new Request(); request.setRequestType(RequestType.ASSIGN_EMPLOYEE);
	 * request.setEmployeeIds(Arrays.asList(1L, 2L)); request.setProjectId(1L);
	 * request.setStatus(RequestStatus.PENDING);
	 * 
	 * Employee employee1 = new Employee(); Employee employee2 = new Employee();
	 * Project project = new Project();
	 * 
	 * when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
	 * when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
	 * when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee2));
	 * when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
	 * when(employeeRepository.save(any(Employee.class))).thenReturn(employee1); //
	 * Assuming saving returns the same employee object
	 * when(requestRepository.save(any(Request.class))).thenReturn(request);
	 * 
	 * Request result = adminService.approveRequest(requestId);
	 * 
	 * assertNotNull(result); assertEquals(RequestStatus.APPROVED,
	 * result.getStatus()); verify(requestRepository).findById(requestId);
	 * verify(employeeRepository).findById(1L);
	 * verify(employeeRepository).findById(2L);
	 * verify(projectRepository).findById(1L); verify(employeeRepository,
	 * times(2)).save(any(Employee.class)); verify(requestRepository).save(request);
	 * }
	 */
    @Test
    void approveRequest_ResourceNotFoundException() {
        Long requestId = 1L;

        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.approveRequest(requestId);
        });

        assertEquals("No request found with Request Id: 1", exception.getMessage());
        verify(requestRepository).findById(requestId);
    }

    @Test
    void approveRequest_Exception() {
        Long requestId = 1L;

        when(requestRepository.findById(requestId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.approveRequest(requestId);
        });

        assertEquals("Internal Server Error", exception.getMessage());
        verify(requestRepository).findById(requestId);
    }

    @Test
    void rejectRequest_Success() {
        Long requestId = 1L;
        Request request = new Request();

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        Request result = adminService.rejectRequest(requestId);

        assertNotNull(result);
        assertEquals(RequestStatus.REJECT, result.getStatus());
        verify(requestRepository).findById(requestId);
        verify(requestRepository).save(request);
    }

    @Test
    void rejectRequest_ResourceNotFoundException() {
        Long requestId = 1L;

        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.rejectRequest(requestId);
        });

        assertEquals("No request found with Request Id: 1", exception.getMessage());
        verify(requestRepository).findById(requestId);
    }

    @Test
    void rejectRequest_Exception() {
        Long requestId = 1L;

        when(requestRepository.findById(requestId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.rejectRequest(requestId);
        });

        assertEquals("Internal Server Error", exception.getMessage());
        verify(requestRepository).findById(requestId);
    }

    @Test
    void deleteEmployee_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        User user = new User();
        user.setUserId(1);
        employee.setUser(user);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteById(employeeId);
        doNothing().when(userRepository).deleteById(1);

        adminService.deleteEmployee(employeeId);

        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).deleteById(employeeId);
        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteEmployee_ResourceNotFoundException() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.deleteEmployee(employeeId);
        });

        assertEquals("Employee not found with employee id: 1", exception.getMessage());
        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void deleteEmployee_Exception() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.deleteEmployee(employeeId);
        });

        assertEquals("Internal Server Error", exception.getMessage());
        verify(employeeRepository).findById(employeeId);
    }
	/*
	 * @Test void updateEmployee_Success() { EmployeeDTO employeeDTO = new
	 * EmployeeDTO(); employeeDTO.setUserId(1); employeeDTO.setManagerId(2L);
	 * employeeDTO.setProjectId(3L); User user = new User(); user.setUserId(1);
	 * Manager manager = new Manager(); manager.setManagerId(2L); Project project =
	 * new Project(); project.setProjectId(3L); Employee employee = new Employee();
	 * 
	 * when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
	 * when(userRepository.findById(1)).thenReturn(Optional.of(user));
	 * when(managerRepository.findById(2L)).thenReturn(Optional.of(manager));
	 * when(projectRepository.findById(3L)).thenReturn(Optional.of(project));
	 * when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
	 * 
	 * Employee result = adminService.updateEmployee(1L, employeeDTO);
	 * 
	 * assertNotNull(result); verify(employeeRepository).findById(1L);
	 * verify(userRepository).findById(1); verify(managerRepository).findById(2L);
	 * verify(projectRepository).findById(3L);
	 * verify(employeeRepository).save(any(Employee.class)); }
	 */
	/*
	 * @Test void updateEmployee_ResourceNotFoundException() { EmployeeDTO
	 * employeeDTO = new EmployeeDTO(); employeeDTO.setUserId(1);
	 * 
	 * when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
	 * 
	 * ResourceNotFoundException exception =
	 * assertThrows(ResourceNotFoundException.class, () -> {
	 * adminService.updateEmployee(1L, employeeDTO); });
	 * 
	 * assertEquals("No Employee found with Employee Id: 1",
	 * exception.getMessage()); verify(employeeRepository).findById(1L); }
	 */

    @Test
    void updateEmployee_Exception() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserId(1);

        when(employeeRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.updateEmployee(1L, employeeDTO);
        });

        assertEquals("Internal Server Error", exception.getMessage());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void getAllRequest_Success() {
        List<Request> requests = Arrays.asList(new Request(), new Request());

        when(requestRepository.findAll()).thenReturn(requests);

        List<Request> result = adminService.getAllRequest();

        assertEquals(2, result.size());
        verify(requestRepository).findAll();
    }

    @Test
    void getAllRequest_Exception() {
        when(requestRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.getAllRequest();
        });

        assertEquals("Internal Server Error", exception.getMessage());
        verify(requestRepository).findAll();
    }

    @Test
    void addManager_Success() {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setUserId(1);
        User user = new User();
        user.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Manager manager = new Manager();
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);

        Manager result = adminService.addmanager(managerDTO);

        assertNotNull(result);
        verify(userRepository).findById(1);
        verify(managerRepository).save(any(Manager.class));
    }

    @Test
    void addManager_ResourceNotFoundException() {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            adminService.addmanager(managerDTO);
        });

        assertEquals("No user found with user id: 1", exception.getMessage());
        verify(userRepository).findById(1);
    }

    @Test
    void addManager_Exception() {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setUserId(1);

        when(userRepository.findById(1)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.addmanager(managerDTO);
        });

        assertEquals("Internal Server Error", exception.getMessage());
        verify(userRepository).findById(1);
    }

    @Test
    void getAllManagers_Success() {
        List<Manager> managers = Arrays.asList(new Manager(), new Manager());

        when(managerService.getAllManagers()).thenReturn(managers);

        List<Manager> result = adminService.getAllManager();

        assertEquals(2, result.size());
        verify(managerService).getAllManagers();
    }

	/*
	 * @Test void getAllManagers_Exception() {
	 * when(managerService.getAllManagers()).thenThrow(new
	 * RuntimeException("Database error"));
	 * 
	 * RuntimeException exception = assertThrows(RuntimeException.class, () -> {
	 * adminService.getAllManager(); });
	 * 
	 * assertEquals("Internal Server Error", exception.getMessage());
	 * verify(managerService).getAllManagers(); }
	 */
   
}
