package com.assignment.employeeManagement.payload.response;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.assignment.employeeManagement.model.UserRole;
import com.assignment.employeeManagement.payload.response.LoginResponse;

public class LoginResponseTest {
	
	@Mock
	private UserRole userRole;
	
    @Test
    public void testConstructorAndGetters() {
       // UserRole userRole = new UserRole();  // Assuming a default constructor exists
        LoginResponse response = new LoginResponse("Login successful", "OK", userRole, 123);

        assertEquals("Login successful", response.getMessage());
        assertEquals("OK", response.getStatus());
        assertEquals(userRole, response.getUserRole());
        assertEquals(123, response.getUserId());
    }

    @Test
    public void testSetters() {
        //UserRole userRole = new UserRole();  // Assuming a default constructor exists
        LoginResponse response = new LoginResponse();

        response.setMessage("Login failed");
        response.setStatus("ERROR");
        response.setUserRole(userRole);
        response.setUserId(456);

        assertEquals("Login failed", response.getMessage());
        assertEquals("ERROR", response.getStatus());
        assertEquals(userRole, response.getUserRole());
        assertEquals(456, response.getUserId());
    }

    @Test
    public void testToString() {
        //UserRole userRole = new UserRole();  // Assuming a default constructor exists
        LoginResponse response = new LoginResponse("Login successful", "OK", userRole, 123);

        String expected = "LoginResponse [message=Login successful, status=OK, userRole=" + userRole + ", userId=123]";
        assertEquals(expected, response.toString());
    }
}
