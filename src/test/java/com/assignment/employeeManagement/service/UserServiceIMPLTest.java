package com.assignment.employeeManagement.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.assignment.employeeManagement.dto.UserAddDTO;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.model.UserRole;
import com.assignment.employeeManagement.repository.UserLoginRepo;

public class UserServiceIMPLTest {
	 @Mock
	    private UserLoginRepo userLoginRepo;

	    @Mock
	    private PasswordEncoder passwordEncoder;

	    @InjectMocks
	    private UserServiceIMPL userService;

	    @BeforeEach
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testAddUser() {
	        UserAddDTO userAddDTO = new UserAddDTO();
	        userAddDTO.setUserId(1);
	        userAddDTO.setUserName("John Doe");
	        userAddDTO.setUserEmail("john@example.com");
	        userAddDTO.setUserPassword("password");
	        userAddDTO.setUserRole(UserRole.EMPLOYEE);

	        User user = new User(1, "John Doe", "john@example.com", "encodedPassword", UserRole.EMPLOYEE);

	        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
	        when(userLoginRepo.save(any(User.class))).thenReturn(user);

	        User addedUser = userService.addUser(userAddDTO);

	        assertEquals("John Doe", addedUser.getUserName());
	        assertEquals("john@example.com", addedUser.getUserEmail());
	        assertEquals("encodedPassword", addedUser.getUserPassword());
	        assertEquals(UserRole.EMPLOYEE, addedUser.getUserRole());

	        verify(userLoginRepo, times(1)).save(any(User.class));
	    }

	    @Test
	    public void testFetchAllUser() {
	        User user1 = new User(1, "John Doe", "john@example.com", "password", UserRole.EMPLOYEE);
	        User user2 = new User(2, "Jane Doe", "jane@example.com", "password", UserRole.MANAGER);

	        when(userLoginRepo.findAll()).thenReturn(Arrays.asList(user1, user2));

	        List<User> users = userService.fetchAllUser();

	        assertEquals(2, users.size());
	        assertTrue(users.contains(user1));
	        assertTrue(users.contains(user2));

	        verify(userLoginRepo, times(1)).findAll();
	    }

	    @Test
	    public void testDeleteAllUser() {
	        doNothing().when(userLoginRepo).deleteAll();

	        userService.deleteAllUser();

	        verify(userLoginRepo, times(1)).deleteAll();
	    }

	    @Test
	    public void testUpdateUser_Success() {
	        UserAddDTO userAddDTO = new UserAddDTO();
	        userAddDTO.setUserName("John Doe Updated");
	        userAddDTO.setUserEmail("john_updated@example.com");
	        userAddDTO.setUserPassword("updatedPassword");
	        userAddDTO.setUserRole(UserRole.EMPLOYEE);

	        User user = new User(1, "John Doe", "john@example.com", "password", UserRole.EMPLOYEE);

	        when(userLoginRepo.findById(1)).thenReturn(Optional.of(user));
	        when(userLoginRepo.save(any(User.class))).thenReturn(user);

	        ResponseEntity<String> response = userService.updateUser(1, userAddDTO);

	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals("User updated successfully", response.getBody());

	        verify(userLoginRepo, times(1)).findById(1);
	        verify(userLoginRepo, times(1)).save(any(User.class));
	    }

	    @Test
	    public void testUpdateUser_UserNotFound() {
	        UserAddDTO userAddDTO = new UserAddDTO();

	        when(userLoginRepo.findById(1)).thenReturn(Optional.empty());

	        ResponseEntity<String> response = userService.updateUser(1, userAddDTO);

	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	        assertEquals("User not found", response.getBody());

	        verify(userLoginRepo, times(1)).findById(1);
	        verify(userLoginRepo, times(0)).save(any(User.class));
	    }

	    @Test
	    public void testUpdateUser_Exception() {
	        UserAddDTO userAddDTO = new UserAddDTO();

	        when(userLoginRepo.findById(1)).thenThrow(new RuntimeException("Database error"));

	        ResponseEntity<String> response = userService.updateUser(1, userAddDTO);

	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	        assertEquals("Failed to update user", response.getBody());

	        verify(userLoginRepo, times(1)).findById(1);
	        verify(userLoginRepo, times(0)).save(any(User.class));
	    }
}
