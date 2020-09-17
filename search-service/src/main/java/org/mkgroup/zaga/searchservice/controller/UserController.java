package org.mkgroup.zaga.searchservice.controller;

import java.io.IOException;
import java.util.List;

import org.mkgroup.zaga.searchservice.dto.NewUserDTO;
import org.mkgroup.zaga.searchservice.model.UserElastic;
import org.mkgroup.zaga.searchservice.repository.UserRepository;
import org.mkgroup.zaga.searchservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/")
public class UserController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserService userService;

	@PostMapping
	public ResponseEntity<?> saveUser(@RequestBody NewUserDTO user) {
		userRepo.save(new UserElastic(user));
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("name")
	public ResponseEntity<?> findByName(@RequestParam(name = "name", required = true, 
													  defaultValue = "") String name)
												      throws IOException {
		List<UserElastic> list = userService.findUserByName(name);
		return new ResponseEntity<List<UserElastic>>(list, HttpStatus.OK);
	}

}
