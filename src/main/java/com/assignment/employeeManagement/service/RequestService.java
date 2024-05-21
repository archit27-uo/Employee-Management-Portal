package com.assignment.employeeManagement.service;

import java.util.List;

import com.assignment.employeeManagement.entity.Request;

public interface RequestService {
	Request addRequest(Request request);
    Request updateRequest(Long requestId, Request request);
    void deleteRequest(Long requestId);
    Request getRequestById(Long requestId);
    List<Request> getAllRequests();
}
