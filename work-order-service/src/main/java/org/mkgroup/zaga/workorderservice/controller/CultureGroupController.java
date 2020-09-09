package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.dto.CultureGroupDTO;
import org.mkgroup.zaga.workorderservice.service.CultureGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/culture/group/")
public class CultureGroupController {
	
	@Autowired
	CultureGroupService cultureGroupService;

	@GetMapping
	public ResponseEntity<?> callSAPCultureGroupSet() throws JSONException {
		return new ResponseEntity<List<CultureGroupDTO>>(
							cultureGroupService.getCultureGroupsFromSAP(),
							HttpStatus.OK);
	}
	
}
