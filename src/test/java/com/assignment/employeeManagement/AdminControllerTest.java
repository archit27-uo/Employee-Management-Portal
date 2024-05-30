package com.assignment.employeeManagement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.assignment.employeeManagement.controller.AdminController;
import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.dto.ProjectDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.service.AdminService;
import com.assignment.employeeManagement.service.EmployeeService;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {
	
	@Mock
	private EmployeeService employeeService;
	
	@Mock
	private AdminService adminService;
	
	@InjectMocks
	private AdminController adminController;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	 @Test
	    public void testAddEmployee() {
	        EmployeeDTO employeeDTO = getEmployeeDTO();
	       
	        Employee employee = createEmployee();
	        when(employeeService.addEmployee(employeeDTO)).thenReturn(employee);

	        Employee result = adminController.addEmployee(employeeDTO);

	        assertEquals(employee, result);
	    }

	    @Test
	    public void testFetchAllEmployee() {
	        List<Employee> employeeList = createEmployeeList();
	        when(employeeService.getAllEmployees()).thenReturn(employeeList);

	        List<Employee> result = adminController.fetchAllEmployee();

	        assertEquals(employeeList, result);
	    }

	    @Test
	    public void testFetchEmployeeById() {
	        Optional<Employee> employee = Optional.ofNullable(getEmployee());
	        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

	        Optional<Employee> result = adminController.fetchEmployeeById(1L);

	        assertEquals(employee, result);
	    }
//
	    @Test
	    public void testDeleteEmployeeById() {
	        ResponseEntity<String> responseEntity = ResponseEntity.ok("Successfully Deleted");

	        ResponseEntity<String> result = adminController.deleteEmployeeById(1L);

	        assertEquals(responseEntity, result);
	    }
//
	    @Test
	    public void testUpdateEmployee() {
	        EmployeeDTO employeeDTO = getEmployeeDTO();
	        Employee employee = getEmployee();
	        when(employeeService.updateEmployee(1L, employeeDTO)).thenReturn(employee);

	        ResponseEntity<Employee> result = adminController.updateEmployee(1L, employeeDTO);

	        assertEquals(ResponseEntity.ok(employee), result);
	    }
//
//	    @Test
//	    public void testAddProject() {
//	        ProjectDTO projectDTO = new ProjectDTO();
//	        Project project = new Project();
//	        when(adminService.addProject(any(ProjectDTO.class))).thenReturn(project);
//
//	        Project result = adminController.addProject(projectDTO);
//
//	        assertEquals(project, result);
//	    }
//
//	    @Test
//	    public void testGetAllProject() {
//	        List<Project> projectList = Arrays.asList(new Project(), new Project());
//	        when(adminService.getAllProjects()).thenReturn(projectList);
//
//	        List<Project> result = adminController.getAllProject();
//
//	        assertEquals(projectList, result);
//	    }
//
//	    @Test
//	    public void testAssignProject() {
//	        Employee employee = new Employee();
//	        when(adminService.assignProjectToEmployee(anyLong(), anyLong())).thenReturn(employee);
//
//	        Employee result = adminController.assignProject(1L, 1L);
//
//	        assertEquals(employee, result);
//	    }
//
//	    @Test
//	    public void testUnassignProject() {
//	        Employee employee = new Employee();
//	        when(adminService.unassignProjectFromEmployee(anyLong())).thenReturn(employee);
//
//	        Employee result = adminController.unassignProject(1L);
//
//	        assertEquals(employee, result);
//	    }
//
//	    @Test
//	    public void testApproveProject() {
//	        Request request = new Request();
//	        when(adminService.approveRequest(anyLong())).thenReturn(request);
//
//	        Request result = adminController.approveProject(1L);
//
//	        assertEquals(request, result);
//	    }
//
//	    @Test
//	    public void testRejectRequest() {
//	        Request request = new Request();
//	        when(adminService.rejectRequest(anyLong())).thenReturn(request);
//
//	        Request result = adminController.rejectRequest(1L);
//
//	        assertEquals(request, result);
//	    }
	    
	    public EmployeeDTO getEmployeeDTO() {
	    	EmployeeDTO employeeDTO = new EmployeeDTO();
	    	 employeeDTO.setEmployeeId(1L);
		        employeeDTO.setFullName("Rahul");
		        Set<String> skills = new HashSet<>();
		        skills.add("Java");
		        skills.add("Spring");
		        employeeDTO.setSkills(skills);
		        return employeeDTO;
	    }
	    
	    public Employee createEmployee() {
	    	Employee employee1 = new Employee();
			employee1.setEmployeeId(1L);
			employee1.setFullName("Rahul");
			Set<String> skillsSet = new HashSet<>();
			skillsSet.add("Java");
			skillsSet.add("Spring");
			employee1.setSkills(skillsSet);
			return employee1;
	    }
	    
	    public List<Employee> createEmployeeList(){
			Employee employee1 = new Employee();
			employee1.setEmployeeId(1L);
			employee1.setFullName("Rahul");
			Set<String> skillsSet = new HashSet<>();
			skillsSet.add("Java");
			skillsSet.add("Spring");
			employee1.setSkills(skillsSet);
			
			
			Employee employee2 = new Employee();
			employee1.setEmployeeId(2L);
			employee1.setFullName("Kishan");
			Set<String> skillsSet2 = new HashSet<>();
			skillsSet2.add("React");
			skillsSet2.add("MongoDB");
			employee1.setSkills(skillsSet2);
			List<Employee> employeeList = new ArrayList<>();
			employeeList.add(employee1);
			employeeList.add(employee2);
			 return employeeList;
	    }
	    
	    public Employee getEmployee() {
	    	Employee employee1 = new Employee();
			employee1.setEmployeeId(1L);
			employee1.setFullName("Rahul");
			Set<String> skillsSet = new HashSet<>();
			skillsSet.add("Java");
			skillsSet.add("Spring");
			employee1.setSkills(skillsSet);
			return employee1;
	    }
	}

