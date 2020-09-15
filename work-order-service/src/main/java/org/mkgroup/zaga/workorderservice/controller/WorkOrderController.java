package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.service.WorkOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("/createWorkOrder")
	public ResponseEntity<?> createWorkOrder(@RequestBody WorkOrderDTO request){
		try {
			workOrderService.addWorkOrder(request);
			return new ResponseEntity<String>("Work order created successfully.", HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<String>("Work order not created.", HttpStatus.BAD_REQUEST);
		}
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
		WorkOrder workOrder = workOrderService.getOne(id);
		ModelMapper modelMapper = new ModelMapper();
		if(workOrder != null) {
			WorkOrderDTO workOrderDTO = new WorkOrderDTO();
			modelMapper.map(workOrder, workOrderDTO);
			return new ResponseEntity<WorkOrderDTO>(workOrderDTO, HttpStatus.OK);
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

}
