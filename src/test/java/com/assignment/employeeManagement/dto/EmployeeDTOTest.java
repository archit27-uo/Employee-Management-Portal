package com.assignment.employeeManagement.dto;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class EmployeeDTOTest {

    @Test
    public void testConstructor() {
        Long employeeId = 1L;
        int userId = 1001;
        String fullName = "John Doe";
        Long projectId = 10L;
        Long managerId = 20L;
        Set<String> skills = new HashSet<>();
        skills.add("Java");
        skills.add("Spring");
        
        EmployeeDTO employeeDTO = new EmployeeDTO(employeeId, userId, fullName, projectId, managerId, skills);
        
        assertEquals(employeeId, employeeDTO.getEmployeeId());
        assertEquals(userId, employeeDTO.getUserId());
        assertEquals(fullName, employeeDTO.getFullName());
        assertEquals(projectId, employeeDTO.getProjectId());
        assertEquals(managerId, employeeDTO.getManagerId());
        assertEquals(skills, employeeDTO.getSkills());
    }
}