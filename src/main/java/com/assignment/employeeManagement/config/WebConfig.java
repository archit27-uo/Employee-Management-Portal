package com.assignment.employeeManagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.assignment.employeeManagement.logging.LoggingInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	 @Autowired
	    private LoggingInterceptor loggingInterceptor;

	    @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(loggingInterceptor);
	    }
}
