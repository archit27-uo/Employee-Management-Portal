package com.assignment.employeeManagement.Security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.model.UserRole;

public class CustomUserDetailsTest {
	
    private User user;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUserEmail("test@example.com");
        user.setUserPassword("password");
        user.setUserRole(UserRole.EMPLOYEE);

        customUserDetails = new CustomUserDetails(user);
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", customUserDetails.getPassword());
    }

    @Test
    public void testGetUsername() {
        assertEquals("test@example.com", customUserDetails.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(customUserDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(customUserDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(customUserDetails.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(customUserDetails.isEnabled());
    }
}
