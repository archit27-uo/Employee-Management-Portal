package com.assignment.employeeManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
	List<Project> findAllByManager(Manager manager);
}
