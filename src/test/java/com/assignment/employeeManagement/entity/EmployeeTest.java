package com.assignment.employeeManagement.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.User;

public class EmployeeTest {
	 @Mock
	 private User user;
	 @Mock
	 private Project project;
	 @Mock
	 private Manager manager;
	 private Set<String> skills;
	 
	 @BeforeEach
	 public void setUp(){
		 skills = new HashSet<>();
		 skills.add("Java");
		 skills.add("Spring Boot");
	 }
	 
	 @Test
	 public void testEmployeeCreation() {
		 Employee employee = new Employee(1L, user, "Ram Kishan", project, manager, skills);
		 assertNotNull(employee);
		 assertEquals(1L, (long)employee.getEmployeeId());
	     assertEquals(user, employee.getUser());
	     assertEquals("Ram Kishan", employee.getFullName());
	     assertEquals(project, employee.getProject());
	     assertEquals(manager, employee.getManager());
	     assertEquals(skills, employee.getSkills());
	 }
	 
	 @Test
	 public void testEmployeeToString() {
		 Employee employee = new Employee(3L, user, "abc", project, manager, skills);
	     String expected = "Employee [employeeId=3, user=" + user + ", fullName=abc, project=" + project + ", manager=" + manager + ", skills=" + skills + "]";
	     assertEquals(expected, employee.toString());
	 }
	 
	 @Test
	 public void testGettersAndSetters() {
	        Employee employee = new Employee();
	        employee.setEmployeeId(2L);
	        employee.setUser(user);
	        employee.setFullName("Jane Smith");
	        employee.setProject(project);
	        employee.setManager(manager);
	        employee.setSkills(skills);

	        assertEquals(2L,(long) employee.getEmployeeId());
	        assertEquals(user, employee.getUser());
	        assertEquals("Jane Smith", employee.getFullName());
	        assertEquals(project, employee.getProject());
	        assertEquals(manager, employee.getManager());
	        assertEquals(skills, employee.getSkills());
	 }
	    @Test
	    public void testEmployeeWithNoProjectOrManager() {
	        Employee employee = new Employee(4L, user, "Bob Brown", null, null, skills);

	        assertNotNull(employee);
	        assertEquals(4L, (long)employee.getEmployeeId());
	        assertEquals(user, employee.getUser());
	        assertEquals("Bob Brown", employee.getFullName());
	        assertNull(employee.getProject());
	        assertNull(employee.getManager());
	        assertEquals(skills, employee.getSkills());
	    }
	 
}
