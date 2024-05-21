package com.assignment.employeeManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.employeeManagement.entity.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
	
}
