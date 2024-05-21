package com.assignment.employeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.assignment.employeeManagement.entity.Employee;

@EnableJpaRepositories
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
//	List<Employee> findByProjectIdIsNull();
//	List<Employee> findByManagerIdIsNull();
	public Optional<Employee> findByEmployeeId(Long employeeId);
}
