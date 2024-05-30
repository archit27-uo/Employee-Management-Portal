package com.assignment.employeeManagement;

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
import com.assignment.employeeManagement.entity.User;
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

import java.util.HashSet;
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
}
