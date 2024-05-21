package com.assignment.employeeManagement.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.assignment.employeeManagement.dto.UserAddDTO;
import com.assignment.employeeManagement.entity.User;


public interface UserService {
	User addUser(UserAddDTO userAddDTO);
	List<User> fetchAllUser();
	void deleteAllUser();
	ResponseEntity<String> updateUser(int userId, UserAddDTO userAddDTO);
}
