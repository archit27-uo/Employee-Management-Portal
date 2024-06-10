package com.assignment.employeeManagement.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	
	private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus status, String message) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", message);
        errorDetails.put("status", status.value());
        return new ResponseEntity<>(errorDetails, status);
    }

	
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    	 return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EmployeeAlreadyAssignedException.class)
    public ResponseEntity<Map<String, Object>> employeeAlreadyAssignedException(EmployeeAlreadyAssignedException ex, WebRequest request) {
    	return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ManagerAlreadyAssignedException.class)
    public ResponseEntity<Map<String, Object>> managerAlreadyAssignedException(ManagerAlreadyAssignedException ex, WebRequest request) {
    	 return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
    	 return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> globalExceptionHandler(Exception ex, WebRequest request) {
    	return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
}