package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.mkgroup.zaga.workorderservice.dto.MaterialDTO;
import org.mkgroup.zaga.workorderservice.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/material/")
public class MaterialController {
	
	@Autowired
	MaterialService materialService;
	
	@GetMapping
	public ResponseEntity<?> callSAPEmployeeSet(){
		return new ResponseEntity<List<MaterialDTO>>(
				materialService.getMaterialsFromSAP(), HttpStatus.OK);
	}

}
