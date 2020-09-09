package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.dto.OperationDTO;
import org.mkgroup.zaga.workorderservice.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operation/")
public class OperationController {
	
	@Autowired
	OperationService operationService;
	
	@GetMapping
	public ResponseEntity<?> callSAPOperationSet() throws JSONException {
		return new ResponseEntity<List<OperationDTO>>(
							operationService.getOperationsFromSAP(),
							HttpStatus.OK);
	}

}
