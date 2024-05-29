package com.assignment.employeeManagement.Security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.assignment.employeeManagement.entity.User;

public class CustomUserDetails implements UserDetails{

	
	private User user;
	
	
	
	public CustomUserDetails(User user) {
		super();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name()));	
	}
	
	@Override
	public String getPassword() {
		
		return user.getUserPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getUserEmail();	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
