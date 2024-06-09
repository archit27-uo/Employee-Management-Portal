package com.assignment.employeeManagement.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ManagerAlreadyAssignedExceptionTest {

    @Test
    public void testExceptionMessage() {
        String expectedMessage = "Manager is already assigned to a project";
        
        // Verify that the exception is thrown with the correct message
        ManagerAlreadyAssignedException exception = assertThrows(
            ManagerAlreadyAssignedException.class,
            () -> { throw new ManagerAlreadyAssignedException(expectedMessage); }
        );

        // Verify that the message is correctly set in the exception
        assertEquals(expectedMessage, exception.getMessage());
    }
}
