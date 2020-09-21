package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.dto.FieldDTO;
import org.mkgroup.zaga.workorderservice.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/field/")
public class FieldController {
	
	@Autowired
	FieldService fieldService;
	
	@GetMapping
	public ResponseEntity<?> callSAPFieldSet() throws JSONException{
		return new ResponseEntity<List<FieldDTO>>(
				fieldService.getFieldsFromSAP(),
						HttpStatus.OK);
	}
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAll(){
		List<FieldDTO> fields = fieldService.getAll();
		if(fields != null) {
			return new ResponseEntity<List<FieldDTO>>(fields, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}