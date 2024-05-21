package com.assignment.employeeManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.employeeManagement.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{

}
