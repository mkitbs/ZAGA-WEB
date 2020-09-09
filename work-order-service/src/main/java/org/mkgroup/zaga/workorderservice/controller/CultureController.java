package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.dto.CultureDTO;
import org.mkgroup.zaga.workorderservice.service.CultureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/culture/")
public class CultureController {
	
	@Autowired
	CultureService cultureService;

	@GetMapping
	public ResponseEntity<?> callSAPCultureSet() throws JSONException {
		
		return new ResponseEntity<List<CultureDTO>>(
				cultureService.getCulturesFromSAP(),
							HttpStatus.OK);
	}
}
