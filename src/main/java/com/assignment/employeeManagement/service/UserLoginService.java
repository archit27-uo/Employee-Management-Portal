package com.assignment.employeeManagement.service;

import com.assignment.employeeManagement.dto.UserLoginDTO;
import com.assignment.employeeManagement.payload.response.LoginResponse;

public interface UserLoginService {
	LoginResponse loginEmployee(UserLoginDTO userLoginDTO);
}
