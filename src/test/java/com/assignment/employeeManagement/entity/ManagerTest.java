package com.assignment.employeeManagement.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ManagerTest {

	@Mock
	private User user;

	 	@Test
	    public void testToString() {
	        Manager manager = new Manager(3L, user);
	        String expected = "Manager [managerId=3, user=" + user + "]";
	        assertEquals(expected, manager.toString());
	    }
	 
	 	@Test
	    public void testManagerCreation() {
	        Manager manager = new Manager(1L, user);

	        assertNotNull(manager);
	        assertEquals(1L, manager.getManagerId().longValue());
	        assertEquals(user, manager.getUser());
	    }

	    @Test
	    public void testGettersAndSetters() {
	        Manager manager = new Manager();
	        manager.setManagerId(2L);
	        manager.setUser(user);

	        assertEquals(2L, manager.getManagerId().longValue());
	        assertEquals(user, manager.getUser());
	    }
}
