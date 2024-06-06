package com.assignment.employeeManagement.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ManagerDTOTest {

    @Test
    public void testManagerDTOConstructorAndGetters() {
        int userId = 1;

        ManagerDTO managerDTO = new ManagerDTO(userId);

        assertEquals(userId, managerDTO.getUserId());
    }

    @Test
    public void testManagerDTOSetters() {
        ManagerDTO managerDTO = new ManagerDTO();

        int userId = 1;
        managerDTO.setUserId(userId);

        assertEquals(userId, managerDTO.getUserId());
    }

    @Test
    public void testToString() {
        int userId = 1;

        ManagerDTO managerDTO = new ManagerDTO(userId);
        String expectedString = "ManagerDTO [userId=" + userId + "]";

        assertEquals(expectedString, managerDTO.toString());
    }
}
