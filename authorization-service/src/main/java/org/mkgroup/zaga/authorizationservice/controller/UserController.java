package org.mkgroup.zaga.authorizationservice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.mkgroup.zaga.authorizationservice.dto.RoleDTO;
import org.mkgroup.zaga.authorizationservice.dto.UserDTO;
import org.mkgroup.zaga.authorizationservice.model.Role;
import org.mkgroup.zaga.authorizationservice.model.User;
import org.mkgroup.zaga.authorizationservice.repository.RoleRepository;
import org.mkgroup.zaga.authorizationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
		User logged = new User();
		if(userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent()) {
    		logged = userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
    	}
		List<UserDTO> retValues = new ArrayList<UserDTO>();
		for(User user : users) {
			if(logged.getTenant().equals(user.getTenant())) {
				UserDTO userDTO = new UserDTO(user);
				retValues.add(userDTO);
			}
			
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
	
	@GetMapping("getUserBySapId/{id}")
	public ResponseEntity<?> getUserBySapId(@PathVariable String id){
		if(userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent()) {
    		User logged = userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
    		User user = userRepo.findBySapUserId(id, logged.getTenant().getId());
    		UserDTO userDTO = new UserDTO(user);
    		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	

	@GetMapping("getUserById/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id){
		User user = userRepo.getOne(id);
		UserDTO userDTO = new UserDTO(user);
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
	}
}
