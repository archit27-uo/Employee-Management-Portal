package com.assignment.employeeManagement.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.assignment.employeeManagement.dto.RequestDTO;
import com.assignment.employeeManagement.model.RequestType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class RequestDTOTest {

    @Test
    public void testRequestDTOConstructorAndGetters() {
        Long requesterId = 1L;
        RequestType requestType = RequestType.ASSIGN_EMPLOYEE;
        Long projectId = 2L;
        List<Long> employeeIds = Arrays.asList(3L, 4L, 5L);
        String requestDetails = "Test details";

        RequestDTO requestDTO = new RequestDTO(requesterId, requestType, projectId, employeeIds, requestDetails);

        assertEquals(requesterId, requestDTO.getRequesterId());
        assertEquals(requestType, requestDTO.getRequestType());
        assertEquals(projectId, requestDTO.getProjectId());
        assertEquals(employeeIds, requestDTO.getEmployeeIds());
        assertEquals(requestDetails, requestDTO.getRequestDetails());
    }

    @Test
    public void testRequestDTOSetters() {
        RequestDTO requestDTO = new RequestDTO();

        Long requesterId = 1L;
        RequestType requestType = RequestType.ASSIGN_EMPLOYEE;
        Long projectId = 2L;
        List<Long> employeeIds = Arrays.asList(3L, 4L, 5L);
        String requestDetails = "Test details";

        requestDTO.setRequesterId(requesterId);
        requestDTO.setRequestType(requestType);
        requestDTO.setProjectId(projectId);
        requestDTO.setEmployeeIds(employeeIds);
        requestDTO.setRequestDetails(requestDetails);

        assertEquals(requesterId, requestDTO.getRequesterId());
        assertEquals(requestType, requestDTO.getRequestType());
        assertEquals(projectId, requestDTO.getProjectId());
        assertEquals(employeeIds, requestDTO.getEmployeeIds());
        assertEquals(requestDetails, requestDTO.getRequestDetails());
    }

    @Test
    public void testToString() {
        Long requesterId = 1L;
        RequestType requestType = RequestType.ASSIGN_EMPLOYEE;
        Long projectId = 2L;
        List<Long> employeeIds = Arrays.asList(3L, 4L, 5L);
        String requestDetails = "Test details";

        RequestDTO requestDTO = new RequestDTO(requesterId, requestType, projectId, employeeIds, requestDetails);
        String expectedString = "RequestDTO [requesterId=1, requestType=ASSIGN_EMPLOYEE, projectId=2, employeeIds=[3, 4, 5], requestDetails=Test details]";

        assertEquals(expectedString, requestDTO.toString());
    }
}
