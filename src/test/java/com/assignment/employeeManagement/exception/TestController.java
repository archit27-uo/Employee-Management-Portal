package com.assignment.employeeManagement.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
class TestController {

	@GetMapping("/resource-not-found")
    public void throwResourceNotFoundException() {
        throw new ResourceNotFoundException("Resource not found");
    }

    @GetMapping("/employee-already-assigned")
    public void throwEmployeeAlreadyAssignedException() {
        throw new EmployeeAlreadyAssignedException("Employee is already assigned to a project");
    }

    @GetMapping("/manager-already-assigned")
    public void throwManagerAlreadyAssignedException() {
        throw new ManagerAlreadyAssignedException("Manager is already assigned to a project");
    }

    @GetMapping("/illegal-argument")
    public void throwIllegalArgumentException() {
        throw new IllegalArgumentException("Illegal argument");
    }

    @GetMapping("/global-exception")
    public void throwGlobalException() {
        throw new RuntimeException("Internal server error");
    }
}