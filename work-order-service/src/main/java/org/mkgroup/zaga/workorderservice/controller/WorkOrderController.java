package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.DateDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.CloseWorkOrderResponse;
import org.mkgroup.zaga.workorderservice.dtoSAP.SAPResponse;
import org.mkgroup.zaga.workorderservice.feign.SAP4HanaProxy;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.service.WorkOrderService;
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
	
	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAP4HanaProxy sap4hana;
	
	@PostMapping("/createWorkOrder")
	public ResponseEntity<?> createWorkOrder(@RequestBody WorkOrderDTO request) throws Exception{
		try {
			SAPResponse sapResponse = workOrderService.addWorkOrder(request);
			
			if(sapResponse.isSuccess()) {
				return new ResponseEntity<SAPResponse>(sapResponse ,HttpStatus.OK);
			}else {
				return new ResponseEntity<SAPResponse>(sapResponse, HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/createCopy/{id}")
	public ResponseEntity<?> copyWorkOrder(@PathVariable UUID id, @RequestBody DateDTO date){
		WorkOrder workOrder = wrepo.getOne(id);
	
		WorkOrder copy;
		try {
			copy = workOrderService.createCopy(workOrder, date);
			return new ResponseEntity<UUID>(copy.getId(), HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>("Work order not updated. Error " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/closeWorkOrder")
	public ResponseEntity<?> closeWorkOrder(@RequestBody WorkOrderDTO request){
		try {
			CloseWorkOrderResponse closeWO =  workOrderService.closeWorkOrder(request);
			if(closeWO.isStatus()) {
				return new ResponseEntity<>(HttpStatus.OK);
			}else {
				return new ResponseEntity<List<String>>(closeWO.getErrors(),HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getAllByStatus/{status}")
	public ResponseEntity<?> getAllByStatus(@PathVariable WorkOrderStatus status){
		List<WorkOrderDTO> workOrders = workOrderService.getAllByStatus(status);
		return new ResponseEntity<List<WorkOrderDTO>>(workOrders, HttpStatus.OK);
	}
}
