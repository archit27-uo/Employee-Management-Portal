package com.assignment.employeeManagement.exception;

import com.assignment.employeeManagement.exception.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void resourceNotFoundException_Handle_ReturnsNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        WebRequest request = Mockito.mock(WebRequest.class);
        ResponseEntity<?> response = globalExceptionHandler.resourceNotFoundException(ex, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody());
    }

    @Test
    void employeeAlreadyAssignedException_Handle_ReturnsConflict() {
        EmployeeAlreadyAssignedException ex = new EmployeeAlreadyAssignedException("Employee already assigned");
        WebRequest request = Mockito.mock(WebRequest.class);
        ResponseEntity<?> response = globalExceptionHandler.employeeAlreadyAssignedException(ex, request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Employee already assigned", response.getBody());
    }

    @Test
    void managerAlreadyAssignedException_Handle_ReturnsConflict() {
        ManagerAlreadyAssignedException ex = new ManagerAlreadyAssignedException("Manager already assigned");
        WebRequest request = Mockito.mock(WebRequest.class);
        ResponseEntity<?> response = globalExceptionHandler.managerAlreadyAssignedException(ex, request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Manager already assigned", response.getBody());
    }

    @Test
    void illegalArgumentException_Handle_ReturnsBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Illegal argument");
        WebRequest request = Mockito.mock(WebRequest.class);
        ResponseEntity<?> response = globalExceptionHandler.illegalArgumentException(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Illegal argument", response.getBody());
    }

    @Test
    void globalExceptionHandler_Handle_ReturnsInternalServerError() {
        Exception ex = new Exception("Internal server error");
        WebRequest request = Mockito.mock(WebRequest.class);
        ResponseEntity<?> response = globalExceptionHandler.globalExceptionHandler(ex, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }
}
