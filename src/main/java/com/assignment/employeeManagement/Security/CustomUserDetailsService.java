package com.assignment.employeeManagement.Security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.repository.UserLoginRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	 
	@Autowired
	private UserLoginRepo userRepository;
	
	@Override
	 public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	        User user = userRepository.findByUserEmail(email);
	        if (user == null) {
	            throw new UsernameNotFoundException("User not found with email: " + email);
	        }
	        return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getUserPassword(),
	                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name())));
	    }
}
