package com.assignment.employeeManagement.exception;

public class ManagerAlreadyAssignedException extends RuntimeException {
    public ManagerAlreadyAssignedException(String message) {
        super(message);
    }
}