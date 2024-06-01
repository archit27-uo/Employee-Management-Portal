package com.assignment.employeeManagement.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.assignment.employeeManagement.Security.CustomUserDetailsService;
import com.assignment.employeeManagement.repository.UserLoginRepo;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private UserLoginRepo userLoginRepo;

    @MockBean
    private AuthenticationConfiguration authConfig;

    @Test
    public void adminEndpointRequiresAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/employee").with(user("ADMIN").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(authenticated().withRoles("ADMIN"));
    }

    @Test
    public void managerEndpointRequiresManagerRole() throws Exception {
        mockMvc.perform(get("/api/manager/employee").with(user("manager").roles("MANAGER")))
                .andExpect(status().isOk())
                .andExpect(authenticated().withRoles("MANAGER"));
    }

    @Test
    public void employeeEndpointRequiresEmployeeRole() throws Exception {
        mockMvc.perform(get("/api/employee/all").with(user("employee").roles("EMPLOYEE")))
                .andExpect(status().isOk())
                .andExpect(authenticated().withRoles("EMPLOYEE"));
    }

//    @Test
//    public void employeeInfoEndpointRequiresEmployeeRole() throws Exception {
//        mockMvc.perform(get("/api/employee/info").with(user("employee").roles("EMPLOYEE")))
//                .andExpect(status().isOk())
//                .andExpect(authenticated().withRoles("EMPLOYEE"));
//    }

//    @Test
//    public void loginEndpointIsAccessible() throws Exception {
//    	 mockMvc.perform(formLogin("/api/login").user("arc@mail.com").password("arc123"))
//         .andExpect(status().isOk())
//         .andExpect(authenticated());
//    }
}
