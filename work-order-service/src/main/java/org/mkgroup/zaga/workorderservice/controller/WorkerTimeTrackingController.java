package org.mkgroup.zaga.workorderservice.controller;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.ResponseTimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.dto.TimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerTimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.service.WorkerTimeTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timeTracking/")
public class WorkerTimeTrackingController {
	
	@Autowired
	WorkerTimeTrackingService wtService;

	@GetMapping("getOne/{wowId}")
	public ResponseEntity<WorkerTimeTrackingDTO> getOne(@PathVariable UUID wowId) {
		WorkerTimeTrackingDTO retValue = wtService.getOne(wowId);
		return new ResponseEntity<WorkerTimeTrackingDTO>(retValue, HttpStatus.OK);
	}
	
	@PostMapping("setTracking")
	public ResponseEntity<?> setTracking(@RequestBody TimeTrackingDTO timeTracking, @RequestHeader("SapUserId") String sapUserId){
		ResponseTimeTrackingDTO retValue = wtService.setTracking(timeTracking, sapUserId);
		if(retValue != null) {
			return new ResponseEntity<ResponseTimeTrackingDTO>(retValue, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	
	@PutMapping("updateTracking")
	public ResponseEntity<?> updateTracking(@RequestBody TimeTrackingDTO timeTracking, @RequestHeader("SapUserId") String sapUserId){
		UUID timeTrackingId = wtService.updateTracking(timeTracking, sapUserId);
		if(timeTrackingId != null) {
			return new ResponseEntity<UUID>(timeTrackingId, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
		
}
