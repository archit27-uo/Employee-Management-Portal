package com.assignment.employeeManagement.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Project;

public class ManagerInfoDTOTest {

    @Test
    public void testManagerInfoDTOConstructorAndGetters() {
        Long managerId = 1L;
        String fullName = "John Doe";
        List<Project> projectList = new ArrayList<>();
        List<Employee> employeeList = new ArrayList<>();

        ManagerInfoDTO managerInfoDTO = new ManagerInfoDTO(managerId, fullName, projectList, employeeList);

        assertEquals(managerId, managerInfoDTO.getManagerId());
        assertEquals(fullName, managerInfoDTO.getFullName());
        assertEquals(projectList, managerInfoDTO.getProjectList());
        assertEquals(employeeList, managerInfoDTO.getEmployeeList());
    }

    @Test
    public void testToString() {
        Long managerId = 1L;
        String fullName = "John Doe";
        List<Project> projectList = new ArrayList<>();
        List<Employee> employeeList = new ArrayList<>();

        ManagerInfoDTO managerInfoDTO = new ManagerInfoDTO(managerId, fullName, projectList, employeeList);
        String expectedString = "ManagerInfoDTO [managerId=" + managerId + ", fullName=" + fullName + ", projectList="
                + projectList + ", employeeList=" + employeeList + "]";

        assertEquals(expectedString, managerInfoDTO.toString());
    }
}
