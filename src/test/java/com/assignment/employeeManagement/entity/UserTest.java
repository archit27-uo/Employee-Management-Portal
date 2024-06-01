package com.assignment.employeeManagement.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.assignment.employeeManagement.model.RequestStatus;
import com.assignment.employeeManagement.model.UserRole;

public class UserTest {
 @Test
    public void testUserCreation() {
        User user = new User(1, "JohnDoe", "john@example.com", "password", UserRole.ADMIN);

        assertNotNull(user);
        assertEquals(1, user.getUserId());
        assertEquals("JohnDoe", user.getUserName());
        assertEquals("john@example.com", user.getUserEmail());
        assertEquals("password", user.getUserPassword());
        assertEquals(UserRole.ADMIN, user.getUserRole());
    }
	 @Test
	    public void testGettersAndSetters() {
	        User user = new User();
	        user.setUserId(2);
	        user.setUserName("JaneSmith");
	        user.setUserEmail("jane@example.com");
	        user.setUserPassword("password123");
	        user.setUserRole(UserRole.EMPLOYEE);

	        assertEquals(2, user.getUserId());
	        assertEquals("JaneSmith", user.getUserName());
	        assertEquals("jane@example.com", user.getUserEmail());
	        assertEquals("password123", user.getUserPassword());
	        assertEquals(UserRole.EMPLOYEE, user.getUserRole());
	    }
	 
	 @Test
	    public void testToString() {
	        User user = new User(3, "AliceJohnson", "alice@example.com", "qwerty", UserRole.MANAGER);
	        String expected = "User [userId=3, userName=AliceJohnson, userEmail=alice@example.com, userPassword=qwerty, userRole=MANAGER]";
	        assertEquals(expected, user.toString());
	    }
}
