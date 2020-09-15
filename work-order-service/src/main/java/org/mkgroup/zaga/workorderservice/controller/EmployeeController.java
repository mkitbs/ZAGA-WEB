package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.mkgroup.zaga.workorderservice.dto.EmployeeDTO;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.mkgroup.zaga.workorderservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee/")
public class EmployeeController {
	
	@Autowired
	EmployeeService empService;

	@Autowired
	UserRepository userRepo;
	
	@GetMapping
	public ResponseEntity<?> callSAPEmployeeSet(){
		return new ResponseEntity<List<EmployeeDTO>>(
				empService.getEmployeesFromSAP(), HttpStatus.OK);
	}
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAllUsers(){
		return new ResponseEntity<List<User>>(userRepo.findAll(),HttpStatus.OK);
	}
}
