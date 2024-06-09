package com.assignment.employeeManagement.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ProjectDTOTest {

    @Test
    public void testConstructor() {
        Long projectId = 1L;
        String projectName = "Test Project";
        Long managerId = 2L;
        
        ProjectDTO projectDTO = new ProjectDTO(projectId, projectName, managerId);
        
        assertEquals(projectId, projectDTO.getProjectId());
        assertEquals(projectName, projectDTO.getProjectName());
        assertEquals(managerId, projectDTO.getManagerId());
    }
    
    @Test
    public void testToString() {
        Long projectId = 1L;
        String projectName = "Test Project";
        Long managerId = 2L;
        
        ProjectDTO projectDTO = new ProjectDTO(projectId, projectName, managerId);
        String expectedToString = "ProjectDTO [projectId=" + projectId + ", projectName=" + projectName + ", managerId=" + managerId + "]";
        
        assertEquals(expectedToString, projectDTO.toString());
    }
}

