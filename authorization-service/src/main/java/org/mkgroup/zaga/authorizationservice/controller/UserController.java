package org.mkgroup.zaga.authorizationservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.mkgroup.zaga.authorizationservice.dto.RoleDTO;
import org.mkgroup.zaga.authorizationservice.dto.UserDTO;
import org.mkgroup.zaga.authorizationservice.model.Role;
import org.mkgroup.zaga.authorizationservice.model.User;
import org.mkgroup.zaga.authorizationservice.repository.RoleRepository;
import org.mkgroup.zaga.authorizationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@GetMapping("getAllUsers")
	public ResponseEntity<?> getAllUsers(){
		List<User> users = userRepo.findAll();
		List<UserDTO> retValues = new ArrayList<UserDTO>();
		for(User user : users) {
			UserDTO userDTO = new UserDTO(user);
			retValues.add(userDTO);
		}
		return new ResponseEntity<List<UserDTO>>(retValues, HttpStatus.OK);
	}
	
	@GetMapping("getRoles")
	public ResponseEntity<?> getRoles(){
		List<RoleDTO> retVal = new ArrayList<RoleDTO>();
		List<Role> roles = roleRepo.findAll();
		for(Role r : roles) {
			RoleDTO rdto = new RoleDTO(r);
			retVal.add(rdto);
		}
		return new ResponseEntity<List<RoleDTO>>(retVal,HttpStatus.OK);
	}
}
