package com.assignment.employeeManagement.dto;

public class ProjectDTO {
	
	private Long projectId;
	private String projectName;
	private Long managerId;
	public ProjectDTO(Long projectId, String projectName, Long managerId) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.managerId = managerId;
	}
	public ProjectDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	@Override
	public String toString() {
		return "ProjectDTO [projectId=" + projectId + ", projectName=" + projectName + ", managerId=" + managerId + "]";
	}
	
	
	
}
