package com.assignment.employeeManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.employeeManagement.dto.UserAddDTO;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.service.UserService;


@RestController
@CrossOrigin
@RequestMapping("api")
public class UserContoller {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/user")
	public User saveUser(@RequestBody UserAddDTO userAddDTO) {
		User id = userService.addUser(userAddDTO);
		return id;
	}
	
	@GetMapping("/user")
	public List<User> fetchAllUser(){
		List<User> userList = userService.fetchAllUser();
		return userList;
	}
	
	@DeleteMapping("/user")
	public ResponseEntity<String> deleteUser() {
		userService.deleteAllUser();
		return ResponseEntity.ok("Successfully deleted");
		
	}
	
	@PutMapping("/user/{userId}")
	public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestBody UserAddDTO updatedUser){
		return userService.updateUser(userId, updatedUser);
		
	}
	
}
