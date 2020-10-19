package org.mkgroup.zaga.workorderservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.EmployeeDTO;
import org.mkgroup.zaga.workorderservice.dto.UserElasticDTO;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.mkgroup.zaga.workorderservice.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@GetMapping("getEmployee/{id}")
	public ResponseEntity<?> getEmployee(@PathVariable UUID id){
		User user = empService.getOne(id);
		ModelMapper modelMapper = new ModelMapper();
		if(user != null) {
			EmployeeDTO employee = new EmployeeDTO();
			modelMapper.map(user, employee);
			return new ResponseEntity<EmployeeDTO>(employee, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("getAll")
	public ResponseEntity<?> getAllUsers(){
		List<User> users = userRepo.findAll();
		List<EmployeeDTO> retValues = new ArrayList<EmployeeDTO>();
		for(User user : users) {
			EmployeeDTO emp = new EmployeeDTO(user);
			retValues.add(emp);
		}
		return new ResponseEntity<List<EmployeeDTO>>(retValues,HttpStatus.OK);
	}
	
	@PostMapping("editUser")
	public ResponseEntity<?> editUser(@RequestBody UserElasticDTO emp){
		empService.editUser(emp);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
