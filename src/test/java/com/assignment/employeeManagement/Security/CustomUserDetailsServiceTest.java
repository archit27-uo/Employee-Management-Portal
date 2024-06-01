package com.assignment.employeeManagement.Security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.assignment.employeeManagement.Security.CustomUserDetailsService;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.model.UserRole;
import com.assignment.employeeManagement.repository.UserLoginRepo;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserLoginRepo userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        User user = new User();
        user.setUserEmail("test@example.com");
        user.setUserPassword("password");
        user.setUserRole(UserRole.EMPLOYEE);

        when(userRepository.findByUserEmail("test@example.com")).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_EMPLOYEE")),
            userDetails.getAuthorities()
        );
    }


    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUserEmail("abct@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("abc@example.com");
        });
    }	
}
