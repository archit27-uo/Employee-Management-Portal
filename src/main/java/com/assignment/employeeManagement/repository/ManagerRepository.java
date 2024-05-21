package com.assignment.employeeManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.assignment.employeeManagement.entity.Manager;

@EnableJpaRepositories
@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long>{

}
