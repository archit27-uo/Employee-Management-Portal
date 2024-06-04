package com.assignment.employeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Project;
import com.assignment.employeeManagement.entity.User;

@EnableJpaRepositories
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
//	List<Employee> findByProjectIdIsNull();
	List<Employee> findByManagerIsNull();
	public Optional<Employee> findByEmployeeId(Long employeeId);
	
	List<Employee> findBySkillsIn(List<String> skills);
	
	List<Employee> findByProjectIsNull();
	
	Employee findByUser(User user);
	
	List<Employee> findAllByProject(Project project);
	
	List<Employee> findAllByManager(Manager manager);
	
}
