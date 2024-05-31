package com.assignment.employeeManagement.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.dto.UserLoginDTO;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.model.UserRole;
import com.assignment.employeeManagement.payload.response.LoginResponse;
import com.assignment.employeeManagement.repository.UserLoginRepo;

@Service
public class UserLoginServiceIMPL implements UserLoginService{

	
	@Autowired
	private UserLoginRepo userLoginRepo; 
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public LoginResponse loginEmployee(UserLoginDTO userLoginDTO) {
		String msg = "";
	 //System.out.println(employeeLoginDTO.getEmployeeEmail());
        User user = userLoginRepo.findByUserEmail(userLoginDTO.getUserName());
      // System.out.println(employee1);
        if (user!= null) {
            String password = userLoginDTO.getUserPassword();
            String encodedPassword = user.getUserPassword();
            
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                java.util.Optional<User> userData =  userLoginRepo.findOneByUserEmailAndUserPassword(userLoginDTO.getUserName(), encodedPassword);
                if (userData.isPresent()) {
                    return new LoginResponse("Login Success", "Success",user.getUserRole(), user.getUserId());
                } else {
                    return new LoginResponse("Login Failed", "Failed", UserRole.EMPLOYEE ,-1);
                }
            } else {
                return new LoginResponse("password Not Match", "Failed",UserRole.EMPLOYEE,-1);
            }
        }else {
            return new LoginResponse("Email not exits", "Failed",UserRole.EMPLOYEE,-1);
        }
	}

}
