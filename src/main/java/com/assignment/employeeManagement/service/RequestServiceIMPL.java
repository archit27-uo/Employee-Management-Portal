package com.assignment.employeeManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.repository.RequestRepository;

@Service
public class RequestServiceIMPL implements RequestService {
	
	@Autowired
	private RequestRepository requestRepository;
	
	public Request addRequest(Request request) {
		// TODO Auto-generated method stub
		return null;
	}

	public Request updateRequest(Long requestId, Request request) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteRequest(Long requestId) {
		// TODO Auto-generated method stub
		System.out.println("added"+requestId);
	}

	public Request getRequestById(Long requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Request> getAllRequests() {
		// TODO Auto-generated method stub
		return null;
	}

}
