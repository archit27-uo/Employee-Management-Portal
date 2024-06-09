package com.assignment.employeeManagement.dto;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserLoginDTOTest {

    @Test
    public void testConstructor() {
        String userName = "john_doe";
        String userPassword = "password123";
        
        UserLoginDTO userLoginDTO = new UserLoginDTO(userName, userPassword);
        
        assertEquals(userName, userLoginDTO.getUserName());
        assertEquals(userPassword, userLoginDTO.getUserPassword());
    }
}
