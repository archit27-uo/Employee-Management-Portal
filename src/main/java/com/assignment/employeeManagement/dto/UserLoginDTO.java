package com.assignment.employeeManagement.dto;

public class UserLoginDTO {
	private String userName;
	private String userPassword;
	public UserLoginDTO(String userName, String userPassword) {
		super();
		this.userName = userName;
		this.userPassword = userPassword;
	}
	public UserLoginDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	@Override
	public String toString() {
		return "UserLoginDTO [userName=" + userName + ", userPassword=" + userPassword + "]";
	}
	
	
	
	
}
