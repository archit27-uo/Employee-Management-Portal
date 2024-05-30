package com.assignment.employeeManagement;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.assignment.employeeManagement.controller.EmployeeController;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.service.EmployeeService;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTests {

	@Mock
	private EmployeeService employeeService;
	
	@InjectMocks
	private EmployeeController employeeController;
	
	@BeforeEach
	public void setUp() {
		
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testFetchAllEmployee() {
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
		
		when(employeeService.getAllEmployees()).thenReturn(employeeList);
		List<Employee> result = employeeController.fetchAllEmployee();
		assertEquals(employeeList, result);
	}
	
    @Test
    public void testGetInfo() {
        Principal principal = () -> "user";
        Employee employee = createEmployee();
        when(employeeService.getEmployeeInfo(principal)).thenReturn(employee);

        Employee result = employeeController.getInfo(principal);

        assertEquals(employee, result);
    }
    
    @Test
    void testUpdateSkills() {
        Principal principal = () -> "user";
        List<String> skills = new ArrayList<>();
        skills.add("Flutter");
        skills.add("Android");
        Employee employee = createEmployee();
        when(employeeService.updateSkills(principal, skills)).thenReturn(employee);
        Employee result = employeeController.updateSkills(principal, skills);
        assertEquals(employee, result);
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
}
