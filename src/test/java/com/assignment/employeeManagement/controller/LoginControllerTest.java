package com.assignment.employeeManagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.assignment.employeeManagement.dto.UserLoginDTO;
import com.assignment.employeeManagement.payload.response.LoginResponse;
import com.assignment.employeeManagement.service.UserLoginService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserLoginService userLoginService;

    @InjectMocks
    private LoginController loginController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    public void testLoginEmployee() throws Exception {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserName("test@example.com");
        userLoginDTO.setUserPassword("password");

        LoginResponse loginResponse = new LoginResponse();
       // loginResponse.("fake-jwt-token");
        loginResponse.setUserId(1);
        loginResponse.setMessage("Test User");

        when(userLoginService.loginEmployee(any(UserLoginDTO.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginDTO)))
                .andExpect(status().isOk());
    }

}
