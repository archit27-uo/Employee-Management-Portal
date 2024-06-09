package com.assignment.employeeManagement.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.assignment.employeeManagement.model.UserRole;

public class UserAddDTOTest {

    @Test
    public void testConstructor() {
        int userId = 1;
        String userName = "john_doe";
        String userEmail = "john@example.com";
        String userPassword = "password123";
        UserRole userRole = UserRole.ADMIN;
        
        UserAddDTO userAddDTO = new UserAddDTO(userId, userName, userEmail, userPassword, userRole);
        
        assertEquals(userId, userAddDTO.getUserId());
        assertEquals(userName, userAddDTO.getUserName());
        assertEquals(userEmail, userAddDTO.getUserEmail());
        assertEquals(userPassword, userAddDTO.getUserPassword());
        assertEquals(userRole, userAddDTO.getUserRole());
    }
}
