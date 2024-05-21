package com.assignment.employeeManagement.service;

import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.assignment.employeeManagement.dto.UserAddDTO;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.repository.UserLoginRepo;

@Service
public class UserServiceIMPL implements UserService{

	
	@Autowired
	private UserLoginRepo userLoginRepo; 
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public User addUser(UserAddDTO userAddDTO) {
		User user = new User(
				userAddDTO.getUserId(),
				userAddDTO.getUserName(),
				userAddDTO.getUserEmail(),
				this.passwordEncoder.encode(userAddDTO.getUserPassword()),
				userAddDTO.getUserRole()
				);
		
		userLoginRepo.save(user);
		return user;
	}
	
	@Override
	public List<User> fetchAllUser() {
		List<User> user = (List<User>) userLoginRepo.findAll();
		return user;
	}

	@Override
	public void deleteAllUser() {
		userLoginRepo.deleteAll();
	}

	@Override
	public ResponseEntity<String> updateUser(int userId, UserAddDTO userAddDTO) {
		 try {
	            
	            java.util.Optional<User> optionalUser = userLoginRepo.findById(userId);
	            if (!optionalUser.isPresent()) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	            }
	            User existingUser = optionalUser.get();
	            existingUser.setUserName(userAddDTO.getUserName());
	            existingUser.setUserEmail(userAddDTO.getUserEmail());
	            existingUser.setUserPassword(userAddDTO.getUserPassword());
	            existingUser.setUserRole(userAddDTO.getUserRole());
	            userLoginRepo.save(existingUser);
	            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
	        }
	            
	}
 
}
