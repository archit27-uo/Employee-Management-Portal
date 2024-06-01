package com.assignment.employeeManagement.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.assignment.employeeManagement.dto.UserLoginDTO;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.model.UserRole;
import com.assignment.employeeManagement.payload.response.LoginResponse;
import com.assignment.employeeManagement.repository.UserLoginRepo;

public class UserLoginServiceIMPLTest {
	 	@Mock
	    private UserLoginRepo userLoginRepo;

	    @Mock
	    private PasswordEncoder passwordEncoder;

	    @InjectMocks
	    private UserLoginServiceIMPL userLoginService;

	    @BeforeEach
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testLoginEmployee_Success() {
	        UserLoginDTO userLoginDTO = new UserLoginDTO();
	        userLoginDTO.setUserName("test@example.com");
	        userLoginDTO.setUserPassword("password");

	        User user = new User();
	        user.setUserEmail("test@example.com");
	        user.setUserPassword("$2a$10$somethingencrypted");
	        user.setUserRole(UserRole.EMPLOYEE);
	        user.setUserId(1);

	        when(userLoginRepo.findByUserEmail("test@example.com")).thenReturn(user);
	        when(passwordEncoder.matches("password", "$2a$10$somethingencrypted")).thenReturn(true);
	        when(userLoginRepo.findOneByUserEmailAndUserPassword("test@example.com", "$2a$10$somethingencrypted"))
	                .thenReturn(Optional.of(user));

	        LoginResponse response = userLoginService.loginEmployee(userLoginDTO);

	        assertEquals("Login Success", response.getMessage());
	        assertEquals("Success", response.getStatus());
	        assertEquals(UserRole.EMPLOYEE, response.getUserRole());
	        assertEquals(1, response.getUserId());
	    }

	    @Test
	    public void testLoginEmployee_PasswordNotMatch() {
	        UserLoginDTO userLoginDTO = new UserLoginDTO();
	        userLoginDTO.setUserName("test@example.com");
	        userLoginDTO.setUserPassword("password");

	        User user = new User();
	        user.setUserEmail("test@example.com");
	        user.setUserPassword("$2a$10$somethingencrypted");
	        user.setUserRole(UserRole.EMPLOYEE);
	        user.setUserId(1);

	        when(userLoginRepo.findByUserEmail("test@example.com")).thenReturn(user);
	        when(passwordEncoder.matches("password", "$2a$10$somethingencrypted")).thenReturn(false);

	        LoginResponse response = userLoginService.loginEmployee(userLoginDTO);

	        assertEquals("password Not Match", response.getMessage());
	        assertEquals("Failed", response.getStatus());
	        assertEquals(UserRole.EMPLOYEE, response.getUserRole());
	        assertEquals(-1, response.getUserId());
	    }

	    @Test
	    public void testLoginEmployee_UserNotFound() {
	        UserLoginDTO userLoginDTO = new UserLoginDTO();
	        userLoginDTO.setUserName("nonexistent@example.com");
	        userLoginDTO.setUserPassword("password");

	        when(userLoginRepo.findByUserEmail("nonexistent@example.com")).thenReturn(null);

	        LoginResponse response = userLoginService.loginEmployee(userLoginDTO);

	        assertEquals("Email not exits", response.getMessage());
	        assertEquals("Failed", response.getStatus());
	        assertEquals(UserRole.EMPLOYEE, response.getUserRole());
	        assertEquals(-1, response.getUserId());
	    }

	    @Test
	    public void testLoginEmployee_LoginFailed() {
	        UserLoginDTO userLoginDTO = new UserLoginDTO();
	        userLoginDTO.setUserName("test@example.com");
	        userLoginDTO.setUserPassword("password");

	        User user = new User();
	        user.setUserEmail("test@example.com");
	        user.setUserPassword("$2a$10$somethingencrypted");
	        user.setUserRole(UserRole.EMPLOYEE);
	        user.setUserId(1);

	        when(userLoginRepo.findByUserEmail("test@example.com")).thenReturn(user);
	        when(passwordEncoder.matches("password", "$2a$10$somethingencrypted")).thenReturn(true);
	        when(userLoginRepo.findOneByUserEmailAndUserPassword("test@example.com", "$2a$10$somethingencrypted"))
	                .thenReturn(Optional.empty());

	        LoginResponse response = userLoginService.loginEmployee(userLoginDTO);

	        assertEquals("Login Failed", response.getMessage());
	        assertEquals("Failed", response.getStatus());
	        assertEquals(UserRole.EMPLOYEE, response.getUserRole());
	        assertEquals(-1, response.getUserId());
	    }
}
