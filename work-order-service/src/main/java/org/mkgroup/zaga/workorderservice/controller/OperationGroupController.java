package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.dto.OperationGroupDTO;
import org.mkgroup.zaga.workorderservice.service.OperationGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operation/group/")
public class OperationGroupController {
	
	@Autowired
	OperationGroupService operationGroupService;

	@GetMapping
	public ResponseEntity<?> callSAPOperationGroupSet() throws JSONException {
		return new ResponseEntity<List<OperationGroupDTO>>(operationGroupService.getOperationGroupsFromSAP(),
				HttpStatus.OK);
	}
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAll(){
		List<OperationGroupDTO> operationGroups = operationGroupService.getAll();
		return new ResponseEntity<List<OperationGroupDTO>>(operationGroups, HttpStatus.OK);
	}

}
