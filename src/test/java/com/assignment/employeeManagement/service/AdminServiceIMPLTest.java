package com.assignment.employeeManagement.service;

import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.dto.ProjectDTO;
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
import com.assignment.employeeManagement.service.AdminServiceIMPL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@RunWith(MockitoJUnitRunner.class)
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

	    @InjectMocks
	    private AdminServiceIMPL adminService;
	    
	   

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void testAddEmployee() {
	        EmployeeDTO employeeDTO = new EmployeeDTO();
	        employeeDTO.setEmployeeId(1L);
	        employeeDTO.setUserId(1);
	        employeeDTO.setManagerId(1L);
	        employeeDTO.setProjectId(1L);
	        employeeDTO.setFullName("Rahul");

	        User user = new User();
	        Manager manager = new Manager();
	        Project project = new Project();
		  
		    	Employee employee1 = new Employee();
				employee1.setEmployeeId(1L);
				employee1.setFullName("Rahul");
				employee1.setManager(manager);
				employee1.setProject(project);
				employee1.setUser(user);
				Set<String> skillsSet = new HashSet<>();
				skillsSet.add("Java");
				skillsSet.add("Spring");
			
	        when(userRepository.findById(1)).thenReturn(Optional.of(user));
	        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
	        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
	        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);

	        Employee result = adminService.addEmployee(employeeDTO);

	        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
	        verify(employeeRepository).save(employeeCaptor.capture());
	        Employee capturedEmployee = employeeCaptor.getValue();

	        assertEquals(user, capturedEmployee.getUser());
	        assertEquals(manager, capturedEmployee.getManager());
	        assertEquals(project, capturedEmployee.getProject());
	        assertEquals("Rahul", capturedEmployee.getFullName());
	    }

	   @Test
	    void testAddProject() {
	        ProjectDTO projectDTO = new ProjectDTO();
	        projectDTO.setManagerId(1L);
	        projectDTO.setProjectName("New Project");

	        Manager manager = new Manager();

	        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

	     
	        Project expectedProject = new Project();
	        expectedProject.setProjectName("New Project");
	        expectedProject.setManager(manager);

	        when(projectRepository.save(ArgumentMatchers.any(Project.class))).thenReturn(expectedProject);

	        Project result = adminService.addProject(projectDTO);

	        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
	        verify(projectRepository).save(projectCaptor.capture());
	        Project capturedProject = projectCaptor.getValue();

	        assertEquals(manager, capturedProject.getManager());
	        assertEquals("New Project", capturedProject.getProjectName());
	    }
	   @Test
	    public void testGetAllProjects() {
	        Project project1 = new Project();
	        Project project2 = new Project();

	        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

	        List<Project> projects = adminService.getAllProjects();

	        assertEquals(2, projects.size());
	        verify(projectRepository, times(1)).findAll();
	    }

	    @Test
	    public void testAssignProjectToEmployee() {
	        Employee employee = new Employee();
	        Project project = new Project();
	        project.setProjectId(1L);

	        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
	        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
	        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

	        Employee updatedEmployee = adminService.assignProjectToEmployee(1L, 1L);

	        assertEquals(project, updatedEmployee.getProject());
	        verify(employeeRepository, times(1)).save(employee);
	    }

	    @Test
	    public void testUnassignProjectFromEmployee() {
	        Employee employee = new Employee();
	        employee.setProject(new Project());

	        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
	        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

	        Employee updatedEmployee = adminService.unassignProjectFromEmployee(1L);

	        assertEquals(null, updatedEmployee.getProject());
	        verify(employeeRepository, times(1)).save(employee);
	    }

	    @Test
	    public void testApproveRequest() {
	        Request request = new Request();
	        request.setRequestId(1L);
	        request.setRequestType(RequestType.ASSIGN_EMPLOYEE);
	        request.setEmployeeIds(Arrays.asList(1L, 2L));
	        request.setProjectId(1L);
	        request.setRequester(new Manager());

	        Employee employee1 = new Employee(1L);
	        Employee employee2 = new Employee(2L);
	        Project project = new Project();

	        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
	        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
	        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee2));
	        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
	        when(requestRepository.save(any(Request.class))).thenReturn(request);

	        Request updatedRequest = adminService.approveRequest(1L);

	        assertEquals(RequestStatus.APPROVED, updatedRequest.getStatus());
	        verify(requestRepository, times(1)).save(request);
	    }

	    @Test
	    public void testRejectRequest() {
	        Request request = new Request();
	        request.setRequestId(1L);

	        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
	        when(requestRepository.save(any(Request.class))).thenReturn(request);

	        Request updatedRequest = adminService.rejectRequest(1L);

	        assertEquals(RequestStatus.REJECT, updatedRequest.getStatus());
	        verify(requestRepository, times(1)).save(request);
	    }

	    @Test
	    public void testDeleteEmployee() {
	        // Mocking data
	        Long employeeId = 1L;
	        Employee employee = new Employee();
	        employee.setEmployeeId(employeeId);

	        User user = new User();
	        user.setUserId(1);
	        employee.setUser(user);

	        // Mocking repository behavior
	        when(employeeRepository.findById(employeeId)).thenReturn(java.util.Optional.of(employee));

	        // Calling the method to be tested
	        adminService.deleteEmployee(employeeId);

	        // Verifying interactions
	        verify(employeeRepository, times(1)).findById(employeeId);
	        verify(employeeRepository, times(1)).deleteById(employeeId);
	        verify(userRepository, times(1)).deleteById(user.getUserId());
	    }

	    @Test
	    public void testUpdateEmployee() {
	        EmployeeDTO employeeDTO = new EmployeeDTO();
	        employeeDTO.setUserId(1);
	        employeeDTO.setFullName("John Doe Updated");
	        employeeDTO.setProjectId(1L);
	        employeeDTO.setManagerId(1L);
	        Set<String> skills = new HashSet<>();
	        skills.add("Java");
	        skills.add("Spring");
	        employeeDTO.setSkills(skills);

	        Employee employee = new Employee();
	        User user = new User();
	        Project project = new Project();
	        Manager manager = new Manager();

	        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
	        when(userRepository.findById(1)).thenReturn(Optional.of(user));
	        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
	        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
	        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

	        Employee updatedEmployee = adminService.updateEmployee(1L, employeeDTO);

	        assertEquals("John Doe Updated", updatedEmployee.getFullName());
	        assertEquals(user, updatedEmployee.getUser());
	        assertEquals(project, updatedEmployee.getProject());
	        assertEquals(manager, updatedEmployee.getManager());
	        assertEquals(skills, updatedEmployee.getSkills());

	        verify(employeeRepository, times(1)).save(employee);
	    }

	    @Test
	    public void testGetAllRequests() {
	        Request request1 = new Request();
	        Request request2 = new Request();

	        when(requestRepository.findAll()).thenReturn(Arrays.asList(request1, request2));

	        List<Request> requests = adminService.getAllRequest();

	        assertEquals(2, requests.size());
	        verify(requestRepository, times(1)).findAll();
	    }
}
