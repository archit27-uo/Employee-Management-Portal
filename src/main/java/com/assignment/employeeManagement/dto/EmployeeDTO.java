package com.assignment.employeeManagement.dto;

import java.util.Set;

import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.User;

public class EmployeeDTO {
	private Long employeeId;
	private int userId;
	private String fullName;
	private Long projectId;
	private Long managerId;
	private Set<String> Skills;
	public EmployeeDTO(Long employeeId, int userId, String fullName, Long projectId, Long managerId,
			Set<String> skills) {
		super();
		this.employeeId = employeeId;
		this.userId = userId;
		this.fullName = fullName;
		this.projectId = projectId;
		this.managerId = managerId;
		Skills = skills;
	}
	public EmployeeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public Set<String> getSkills() {
		return Skills;
	}
	public void setSkills(Set<String> skills) {
		Skills = skills;
	}
	@Override
	public String toString() {
		return "EmployeeDTO [employeeId=" + employeeId + ", userId=" + userId + ", fullName=" + fullName
				+ ", projectId=" + projectId + ", managerId=" + managerId + ", Skills=" + Skills + "]";
	}
	
	
	
}
