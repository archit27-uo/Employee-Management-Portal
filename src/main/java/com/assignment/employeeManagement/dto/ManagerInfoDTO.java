package com.assignment.employeeManagement.dto;

import java.util.List;

import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Project;

public class ManagerInfoDTO {
	
	private Long managerId;
	private String fullName;
	private List<Project> projectList;
	private List<Employee> employeeList;
	
	
	public ManagerInfoDTO(Long managerId, String fullName, List<Project> projectList, List<Employee> employeeList) {
		super();
		this.managerId = managerId;
		this.fullName = fullName;
		this.projectList = projectList;
		this.employeeList = employeeList;
	}
	
	public Long getManagerId() {
		return managerId;
	}
	
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public List<Project> getProjectList() {
		return projectList;
	}
	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}
	public List<Employee> getEmployeeList() {
		return employeeList;
	}
	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}
	
	@Override
	public String toString() {
		return "ManagerInfoDTO [managerId=" + managerId + ", fullName=" + fullName + ", projectList=" + projectList
				+ ", employeeList=" + employeeList + "]";
	}

	public ManagerInfoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
