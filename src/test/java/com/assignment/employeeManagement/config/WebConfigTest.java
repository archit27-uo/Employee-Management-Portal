//package com.assignment.employeeManagement.config;
//
//import static org.hamcrest.CoreMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import com.assignment.employeeManagement.logging.LoggingInterceptor;
//
//@WebMvcTest(WebConfig.class)
//public class WebConfigTest {
//	@Autowired
//    private WebApplicationContext wac;
//
//    @MockBean
//    private LoggingInterceptor loggingInterceptor;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }
//
//    @Test
//    public void testLoggingInterceptorIsRegistered() throws Exception {
//        this.mockMvc.perform(get("/"))
//                .andExpect(status().isOk());
//
//        verify(loggingInterceptor).preHandle(any(), any(), any());
//    }
//}
