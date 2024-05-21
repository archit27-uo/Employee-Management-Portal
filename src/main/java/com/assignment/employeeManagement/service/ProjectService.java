package com.assignment.employeeManagement.service;

import java.util.List;

import com.assignment.employeeManagement.entity.Project;

public interface ProjectService {
	Project addProject(Project project);
    Project updateProject(Long projectId, Project project);
    void deleteProject(Long projectId);
    Project getProjectById(Long projectId);
    List<Project> getAllProjects();
}
