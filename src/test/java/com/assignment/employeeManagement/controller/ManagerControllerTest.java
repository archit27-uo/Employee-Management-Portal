package com.assignment.employeeManagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.assignment.employeeManagement.controller.ManagerController;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.service.ManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class ManagerControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
    private ManagerService managerService;

    @InjectMocks
    private ManagerController managerController;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }
    
    @Test
    public void testGetAllEmployee() {
        List<Employee> employeeList = createEmployeeList();
        when(managerService.getAllEmployees()).thenReturn(employeeList);

        List<Employee> result = managerController.getAllEmployee();

        assertEquals(employeeList, result);
    }
    
    @Test
    public void testGetUnassignedEmployees() {
        List<Employee> employeeList = createEmployeeList();
        when(managerService.getUnassignedEmployees()).thenReturn(employeeList);

        List<Employee> result = managerController.getUnassignedEmployees();

        assertEquals(employeeList, result);
    }
    
    @Test
    public void testFilterAllEmployeesBySkills() {
        List<Employee> employeeList = createEmployeeList();
        List<String> skills = Arrays.asList("Java", "Spring");
        when(managerService.filterEmployeesBySkills(skills)).thenReturn(employeeList);

        List<Employee> result = managerController.filterAllEmployeesBySkills(skills);

        assertEquals(employeeList, result);
    }
    
    @Test
    public void testGetAllManager() throws Exception {
        List<Manager> managers = Arrays.asList(new Manager(), new Manager());

        when(managerService.getAllManagers()).thenReturn(managers);

        mockMvc.perform(get("/api/manager/manager"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllProject() throws Exception {
        List<Project> projects = Arrays.asList(new Project(), new Project());

        when(managerService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/api/manager/project"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRequestEmployeesForProject() throws Exception {
        Request request = new Request();

        when(managerService.requestEmployeesForProject(any(String.class), any(Long.class), any(List.class))).thenReturn(request);

        mockMvc.perform(post("/api/manager/request/employees")
                .param("email", "test@example.com")
                .param("projectId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList(1L, 2L))))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetManagerInfo() throws Exception {
        Manager manager = new Manager();
        Principal principal = () -> "test@example.com";

        when(managerService.getManagerInfo(any(Principal.class))).thenReturn(manager);

        mockMvc.perform(get("/api/manager/info")
                .principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetEmployeeListByProjectId() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());

        when(managerService.getAllEmployeeByProject(eq(1L))).thenReturn(employees);

        mockMvc.perform(get("/api/manager/employee/project/1"))
                .andExpect(status().isOk());
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
}
