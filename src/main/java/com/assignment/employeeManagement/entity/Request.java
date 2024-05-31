package com.assignment.employeeManagement.entity;

import java.util.List;

import com.assignment.employeeManagement.model.RequestStatus;
import com.assignment.employeeManagement.model.RequestType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "requests")
public class Request {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Manager requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type")
    private RequestType requestType;
    
    @Column(name= "project_id", nullable=true)
    private Long projectId;
    
    @Column(name = "employee_id", nullable = true)
    private List<Long> employeeIds;

    @Column(name = "request_details")
    private String requestDetails;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

	public Request(Long requestId, Manager requester, RequestType requestType, Long projectId, List<Long> employeeIds,
			String requestDetails, RequestStatus status) {
		super();
		this.requestId = requestId;
		this.requester = requester;
		this.requestType = requestType;
		this.projectId = projectId;
		this.employeeIds = employeeIds;
		this.requestDetails = requestDetails;
		this.status = status;
	}

	public Request() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Manager getRequester() {
		return requester;
	}

	public void setRequester(Manager requester) {
		this.requester = requester;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public List<Long> getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(List<Long> employeeIds) {
		this.employeeIds = employeeIds;
	}

	public String getRequestDetails() {
		return requestDetails;
	}

	public void setRequestDetails(String requestDetails) {
		this.requestDetails = requestDetails;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Request [requestId=" + requestId + ", requester=" + requester + ", requestType=" + requestType
				+ ", projectId=" + projectId + ", employeeIds=" + employeeIds + ", requestDetails=" + requestDetails
				+ ", status=" + status + "]";
	}
    
    
}