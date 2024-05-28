package com.assignment.employeeManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.assignment.employeeManagement.entity.User;

@EnableJpaRepositories
@Repository
public interface UserLoginRepo extends CrudRepository<User, Integer> {
	Optional<User> findOneByUserEmailAndUserPassword(String userEmail, String userPassword);
	User findByUserEmail(String userEmail);
	
}
