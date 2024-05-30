package com.assignment.employeeManagement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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

import com.assignment.employeeManagement.controller.ManagerController;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.service.ManagerService;

@RunWith(MockitoJUnitRunner.class)
public class ManagerControllerTest {
	
	@Mock
    private ManagerService managerService;

    @InjectMocks
    private ManagerController managerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
