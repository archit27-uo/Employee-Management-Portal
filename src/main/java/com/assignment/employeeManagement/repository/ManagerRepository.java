package com.assignment.employeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.User;

@EnableJpaRepositories
@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long>{
	Optional<Manager> findByUser(User user);
	Manager findByManagerId(Long managerId);
}
