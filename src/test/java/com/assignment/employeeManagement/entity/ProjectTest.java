package com.assignment.employeeManagement.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ProjectTest {
	
	@Mock
	private Manager manager;
	
	@Test
    public void testProjectCreation() {
        Project project = new Project(1L, "Beta", manager);

        assertNotNull(project);
        assertEquals(1L, project.getProjectId().longValue());
        assertEquals("Beta", project.getProjectName());
        assertEquals(manager, project.getManager());
    }
	
	   @Test
	    public void testToString() {
	        Project project = new Project(3L, "Project Gamma", manager);
	        String expected = "Project [projectId=3, projectName=Project Gamma, manager=" + manager + "]";
	        assertEquals(expected, project.toString());
	    }
	   
	    @Test
	    public void testGettersAndSetters() {
	        Project project = new Project();
	        project.setProjectId(2L);
	        project.setProjectName("Project Beta");
	        project.setManager(manager);

	        assertEquals(2L, project.getProjectId().longValue());
	        assertEquals("Project Beta", project.getProjectName());
	        assertEquals(manager, project.getManager());
	    }
}
