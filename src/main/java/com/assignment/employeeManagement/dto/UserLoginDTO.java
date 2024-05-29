package com.assignment.employeeManagement.dto;

public class UserLoginDTO {
	private String userName;
	private String userPassword;
	public UserLoginDTO(String userEmail, String userPassword) {
		super();
		this.userName = userEmail;
		this.userPassword = userPassword;
	}
	public UserLoginDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUserEmail() {
		return userName;
	}
	public void setUserEmail(String userEmail) {
		this.userName = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	@Override
	public String toString() {
		return "UserLoginDTO [userEmail=" + userName + ", userPassword=" + userPassword + "]";
	}
	
	
}
