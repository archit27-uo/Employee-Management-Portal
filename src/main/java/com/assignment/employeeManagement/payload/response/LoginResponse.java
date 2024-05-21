package com.assignment.employeeManagement.payload.response;

import com.assignment.employeeManagement.model.UserRole;

public class LoginResponse {
	private String message;
	private String status;
	private UserRole userRole;
	private int userId;
	public LoginResponse(String message, String status, UserRole userRole, int userId) {
		super();
		this.message = message;
		this.status = status;
		this.userRole = userRole;
		this.userId = userId;
	}
	public LoginResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "LoginResponse [message=" + message + ", status=" + status + ", userRole=" + userRole + ", userId="
				+ userId + "]";
	}
	
}
