package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.mkgroup.zaga.workorderservice.dto.FieldGroupDTO;
import org.mkgroup.zaga.workorderservice.service.FieldGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/field/group/")
public class FieldGroupController {
	
	@Autowired
	FieldGroupService fieldGroupService;
	
	@GetMapping
	public ResponseEntity<?> callSAPFieldGroupSet() throws JSONException{
		return new ResponseEntity<List<FieldGroupDTO>>(
				fieldGroupService.getFieldGroupsFromSAP(),
				HttpStatus.OK);
				
	}
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAllFieldGroups(){
		List<FieldGroupDTO> fieldGroups = fieldGroupService.getAll();
		return new ResponseEntity<List<FieldGroupDTO>>(fieldGroups, HttpStatus.OK);
	}
}
