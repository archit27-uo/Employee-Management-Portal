package com.assignment.employeeManagement.dto;

import java.util.List;

import com.assignment.employeeManagement.model.RequestType;

public class RequestDTO {
	
	private Long requesterId;
	private RequestType requestType;
	private Long projectId;
	private List<Long> employeeIds;
	private String requestDetails;
	public RequestDTO(Long requesterId, RequestType requestType, Long projectId, List<Long> employeeIds,
			String requestDetails) {
		super();
		this.requesterId = requesterId;
		this.requestType = requestType;
		this.projectId = projectId;
		this.employeeIds = employeeIds;
		this.requestDetails = requestDetails;
	}
	public RequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getRequesterId() {
		return requesterId;
	}
	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
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
	@Override
	public String toString() {
		return "RequestDTO [requesterId=" + requesterId + ", requestType=" + requestType + ", projectId=" + projectId
				+ ", employeeIds=" + employeeIds + ", requestDetails=" + requestDetails + "]";
	}
	
	
	
}
