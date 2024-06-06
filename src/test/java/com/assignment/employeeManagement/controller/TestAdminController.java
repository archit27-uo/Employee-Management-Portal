package com.assignment.employeeManagement.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.assignment.employeeManagement.dto.EmployeeDTO;
import com.assignment.employeeManagement.dto.ManagerDTO;
import com.assignment.employeeManagement.dto.ProjectDTO;
import com.assignment.employeeManagement.dto.UserAddDTO;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.service.AdminService;
import com.assignment.employeeManagement.service.EmployeeService;
import com.assignment.employeeManagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
public class TestAdminController {
	
	private MockMvc mockMvc;
	
	@Mock
	private EmployeeService employeeService;
	
	@Mock
    private UserService userService;

	@Mock
	private AdminService adminService;
	
	@InjectMocks
	private AdminController adminController;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private List<Manager> managerList;
	private ManagerDTO managerDTO;
	private Manager manager;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
		manager = new Manager();
        manager.setManagerId(1L);
        User user = new User();
        manager.setUser(user);
        

        managerList = Arrays.asList(manager);

        managerDTO = new ManagerDTO();
       managerDTO.setUserId(1);
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
	    @Test
	    public void testAddProject() throws Exception {
	        ProjectDTO projectDTO = new ProjectDTO();
	        Project project = new Project();

	        when(adminService.addProject(any(ProjectDTO.class))).thenReturn(project);

	        mockMvc.perform(post("/api/admin/project")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(projectDTO)))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testGetAllProjects() throws Exception {
	        List<Project> projects = Arrays.asList(new Project(), new Project());

	        when(adminService.getAllProjects()).thenReturn(projects);

	        mockMvc.perform(get("/api/admin/projects"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testAssignProject() throws Exception {
	        Employee employee = new Employee();

	        when(adminService.assignProjectToEmployee(eq(1L), eq(1L))).thenReturn(employee);

	        mockMvc.perform(put("/api/admin/employee/1/assignProject/1"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testUnassignProject() throws Exception {
	        Employee employee = new Employee();

	        when(adminService.unassignProjectFromEmployee(eq(1L))).thenReturn(employee);

	        mockMvc.perform(put("/api/admin/employee/1/unassignProject"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testApproveProject() throws Exception {
	        Request request = new Request();

	        when(adminService.approveRequest(eq(1L))).thenReturn(request);

	        mockMvc.perform(put("/api/admin/request/1/approve"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testRejectRequest() throws Exception {
	        Request request = new Request();

	        when(adminService.rejectRequest(eq(1L))).thenReturn(request);

	        mockMvc.perform(put("/api/admin/request/1/reject"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testSaveUser() throws Exception {
	        UserAddDTO userAddDTO = new UserAddDTO();
	        User user = new User();

	        when(userService.addUser(any(UserAddDTO.class))).thenReturn(user);

	        mockMvc.perform(post("/api/admin/user")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(userAddDTO)))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testGetAllRequests() throws Exception {
	        List<Request> requests = Arrays.asList(new Request(), new Request());

	        when(adminService.getAllRequest()).thenReturn(requests);

	        mockMvc.perform(get("/api/admin/request"))
	                .andExpect(status().isOk());
	    }
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
	    
	    @Test
	    public void testGetAllManager() throws Exception {
	        when(adminService.getAllManager()).thenReturn(managerList);

	        mockMvc.perform(get("/api/admin/manager"))
	                .andExpect(status().isOk())
	                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	                
	    }

	    @Test
	    public void testAddManager() throws Exception {
	        when(adminService.addmanager(any(ManagerDTO.class))).thenReturn(manager);

	        mockMvc.perform(post("/api/admin/manager")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(managerDTO)))
	                .andExpect(status().isOk())
	                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	               
	    }
	}

