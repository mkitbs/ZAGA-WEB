package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.mkgroup.zaga.workorderservice.dto.CropVarietyDTO;
import org.mkgroup.zaga.workorderservice.service.CropVarietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cropvariety/")
public class CropVarietyController {

	@Autowired
	CropVarietyService cvService;
	
	@GetMapping
	public ResponseEntity<?> callSAPEmployeeSet(){
		return new ResponseEntity<List<CropVarietyDTO>>(
				cvService.getCropVarietiesFromSAP(), HttpStatus.OK);
	}
}
