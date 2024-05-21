package com.assignment.employeeManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.repository.ProjectRepository;

@Service
public class ProjectServiceIMPL implements ProjectService{

	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Override
	public Project addProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project updateProject(Long projectId, Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteProject(Long projectId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Project getProjectById(Long projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Project> getAllProjects() {
		// TODO Auto-generated method stub
		return null;
	}

}
