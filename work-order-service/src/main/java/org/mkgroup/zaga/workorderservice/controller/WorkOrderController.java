package org.mkgroup.zaga.workorderservice.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.joda.time.LocalDate;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.DateDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workOrder")
public class WorkOrderController {
	
	@Autowired
	WorkOrderService workOrderService;
	
	@Autowired
	WorkOrderRepository wrepo;
	
	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@PostMapping("/createWorkOrder")
	public ResponseEntity<?> createWorkOrder(@RequestBody WorkOrderDTO request){
		try {
			workOrderService.addWorkOrder(request);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>("Work order not created.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/createCopy/{id}")
	public ResponseEntity<?> copyWorkOrder(@PathVariable UUID id, @RequestBody DateDTO date){
		WorkOrder workOrder = wrepo.getOne(id);
		
		LocalDate newDate = new LocalDate(Integer.parseInt(date.getYear()),
										  Integer.parseInt(date.getMonth()),
										  Integer.parseInt(date.getDay()));
		System.out.println(newDate.toString());
		WorkOrder copy = workOrderService.createCopy(workOrder, newDate.toDate());
		
		return new ResponseEntity<UUID>(copy.getId(), HttpStatus.CREATED);
	}
	
	@PostMapping("/createTestWorkOrder")
	public ResponseEntity<?> testWO(@RequestBody String t) {
		WorkOrder wo = new WorkOrder();
		wo = wrepo.save(wo);
		//returning as expected, uuid works
		return new ResponseEntity<WorkOrder>(wo, HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllWorkOrders(){
		try {
			List<WorkOrderDTO> workOrders = workOrderService.getAll();
			return new ResponseEntity<List<WorkOrderDTO>>(workOrders, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>("Work orders were not found. Error " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getWorkOrder/{id}")
	public ResponseEntity<?> getWorkOrder(@PathVariable UUID id){
		WorkOrderDTO workOrder = workOrderService.getOne(id);
		if(workOrder != null) {
			return new ResponseEntity<WorkOrderDTO>(workOrder, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/updateWorkOrder")
	public ResponseEntity<?> updateWorkOrder(@RequestBody WorkOrderDTO request){
		try {
			workOrderService.updateWorkOrder(request);
			return new ResponseEntity<String>("Work order updated successfully.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>("Work order not updated. Error " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/closeWorkOrder")
	public ResponseEntity<?> closeWorkOrder(@RequestBody WorkOrderDTO request){
		workOrderService.closeWorkOrder(request);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/test")
	public ResponseEntity<?> test(){
		
		String csrfToken;
		
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		
		ResponseEntity<Object> resp = gwProxy.getCSRFToken("Basic " + authHeader, "Fetch");
		
		HttpHeaders headers = resp.getHeaders();
		csrfToken = headers.getValuesAsList("x-csrf-token").stream()
				                                                 .findFirst()
				                                                 .orElse("nema");
		
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/getAllByStatus/{status}")
	public ResponseEntity<?> getAllByStatus(@PathVariable WorkOrderStatus status){
		List<WorkOrderDTO> workOrders = workOrderService.getAllByStatus(status);
		return new ResponseEntity<List<WorkOrderDTO>>(workOrders, HttpStatus.OK);
	}
}
