package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.mkgroup.zaga.workorderservice.dto.EmployeeDTO;
import org.mkgroup.zaga.workorderservice.dto.VarietyDTO;
import org.mkgroup.zaga.workorderservice.service.VarietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/variety/")
public class VarietyController {

	@Autowired
	VarietyService varietyService;
	
	@GetMapping
	public ResponseEntity<?> callSAPEmployeeSet(){
		return new ResponseEntity<List<VarietyDTO>>(
				varietyService.getVarietiesFromSAP(), HttpStatus.OK);
	}
}
