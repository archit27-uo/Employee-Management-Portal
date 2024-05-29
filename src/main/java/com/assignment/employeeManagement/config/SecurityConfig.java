package com.assignment.employeeManagement.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.assignment.employeeManagement.Security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/manager/**").hasRole("MANAGER")
                .requestMatchers("/api/employee/**").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                .requestMatchers("api/employee/info").hasRole("EMPLOYEE")
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic
                    .realmName("employeeManagement")
                )
            .formLogin(form -> form
                    .loginPage("/login").disable()
//                    .permitAll()
                )
            .csrf(AbstractHttpConfigurer::disable)
            .userDetailsService(userDetailsService);
        
        return http.build();
    }
	
	
    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}