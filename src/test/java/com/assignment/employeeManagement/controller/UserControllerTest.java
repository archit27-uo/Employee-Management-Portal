package com.assignment.employeeManagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.assignment.employeeManagement.dto.UserAddDTO;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

	private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testSaveUser() throws Exception {
        UserAddDTO userAddDTO = new UserAddDTO();
        User user = new User();
        
        when(userService.addUser(any(UserAddDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userAddDTO)))
                .andExpect(status().isOk());
              

        verify(userService, times(1)).addUser(any(UserAddDTO.class));
    }

    @Test
    public void testFetchAllUser() throws Exception {
        User user1 = new User();
        User user2 = new User();
        List<User> userList = Arrays.asList(user1, user2);

        when(userService.fetchAllUser()).thenReturn(userList);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
              

        verify(userService, times(1)).fetchAllUser();
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));

        verify(userService, times(1)).deleteAllUser();
    }

    @Test
    public void testUpdateUser() throws Exception {
        int userId = 1;
        UserAddDTO updatedUser = new UserAddDTO();
        ResponseEntity<String> responseEntity = ResponseEntity.ok("User updated successfully");

        when(userService.updateUser(eq(userId), any(UserAddDTO.class))).thenReturn(responseEntity);

        mockMvc.perform(put("/api/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully"));

        verify(userService, times(1)).updateUser(eq(userId), any(UserAddDTO.class));
    }
}
