package com.assignment.employeeManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.employeeManagement.dto.UserLoginDTO;
import com.assignment.employeeManagement.payload.response.LoginResponse;
import com.assignment.employeeManagement.service.UserLoginService;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class LoginController {
	@Autowired
	private UserLoginService userLoginService;
	
	@PostMapping("/login") 
	public ResponseEntity<LoginResponse> loginEmployee(@RequestBody UserLoginDTO userLoginDTO){
		LoginResponse loginResponse = userLoginService.loginEmployee(userLoginDTO);
		return ResponseEntity.ok(loginResponse);
	}

}
