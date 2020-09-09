package org.mkgroup.zaga.workorderservice.controller;

import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workOrder")
public class WorkOrderController {
	
	@Autowired
	WorkOrderService workOrderService;
	
	@PostMapping("/createWorkOrder")
	public ResponseEntity<?> createWorkOrder(@RequestBody WorkOrderDTO request){
		try {
			workOrderService.addWorkOrder(request);
			return new ResponseEntity<String>("Work order created successfully.", HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<String>("Work order not created.", HttpStatus.BAD_REQUEST);
		}
	}
}
