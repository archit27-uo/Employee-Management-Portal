package com.assignment.employeeManagement.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(LoggingInterceptor.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }
   
}
