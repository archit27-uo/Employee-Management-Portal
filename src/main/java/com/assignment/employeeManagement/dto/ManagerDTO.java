package com.assignment.employeeManagement.dto;

public class ManagerDTO {
	
	private int userId;

	public ManagerDTO(int userId) {
		super();
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	
	public ManagerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ManagerDTO [userId=" + userId + "]";
	}
	
}
