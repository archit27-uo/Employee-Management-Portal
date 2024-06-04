package com.assignment.employeeManagement.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.assignment.employeeManagement.model.RequestStatus;
import com.assignment.employeeManagement.model.RequestType;

public class RequestTest {

	@Mock
	private Manager requester;
	private List<Long> employeeIds;
	
	@BeforeEach
	public void setUp() {
		employeeIds = Arrays.asList(1L,2L,3L);
	}
	
    @Test
    public void testRequestCreation() {
        Request request = new Request(1L, requester, RequestType.ASSIGN_EMPLOYEE, 201L, employeeIds, "Request for new hires", RequestStatus.PENDING);

        assertNotNull(request);
        assertEquals(1L, request.getRequestId().longValue());
        assertEquals(requester, request.getRequester());
        assertEquals(RequestType.ASSIGN_EMPLOYEE, request.getRequestType());
        assertEquals(201L, request.getProjectId().longValue());
        assertEquals(employeeIds, request.getEmployeeIds());
        assertEquals("Request for new hires", request.getRequestDetails());
        assertEquals(RequestStatus.PENDING, request.getStatus());
    }
    
    @Test
    public void testGettersAndSetters() {
        List<Long> employeeIds = new ArrayList<>();
        employeeIds.add(3L);
        employeeIds.add(4L);

        Request request = new Request();
        request.setRequestId(2L);
        request.setRequester(requester);
        request.setRequestType(RequestType.ASSIGN_EMPLOYEE);
        request.setProjectId(2L);
        request.setEmployeeIds(employeeIds);
        request.setRequestDetails("Training request");
        request.setStatus(RequestStatus.APPROVED);

        assertEquals(2L, request.getRequestId().longValue());
        assertEquals(requester, request.getRequester());
        assertEquals(RequestType.ASSIGN_EMPLOYEE, request.getRequestType());
        assertEquals(2L, request.getProjectId().longValue());
        assertEquals(employeeIds, request.getEmployeeIds());
        assertEquals("Training request", request.getRequestDetails());
        assertEquals(RequestStatus.APPROVED, request.getStatus());
    }
    
    @Test
    public void testToString() {
        Request request = new Request(3L, requester, RequestType.ASSIGN_EMPLOYEE, null, null, "Meeting request", RequestStatus.REJECT);
        String expected = "Request [requestId=3, requester=" + requester + ", requestType=EMPLOYEE, projectId=null, employeeIds=null, requestDetails=Meeting request, status=REJECT]";
        assertEquals(expected, request.toString());
    }
}
