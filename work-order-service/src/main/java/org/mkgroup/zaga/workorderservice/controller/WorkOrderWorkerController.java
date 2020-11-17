package org.mkgroup.zaga.workorderservice.controller;

import java.util.Date;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.OperationRepository;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.mkgroup.zaga.workorderservice.service.WorkOrderWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workOrderWorker/")
public class WorkOrderWorkerController {
	
	@Autowired
	WorkOrderWorkerRepository wowRepo;
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	@Autowired
	OperationRepository operationRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	WorkOrderWorkerService wowService;
	
	@Autowired
	MachineRepository machineRepo;

	@PostMapping("addWorker/{id}")
	public ResponseEntity<?> addWorker(@PathVariable UUID id,@RequestBody WorkOrderWorkerDTO wowDTO) throws Exception{
			wowService.addWorker(id, wowDTO);
			return new ResponseEntity<>(HttpStatus.OK);
		
		
	}
	
	@PostMapping("updateWorkOrderWorker/{id}")
	public ResponseEntity<?> updateWorkOrderWorker(@PathVariable UUID id, @RequestBody WorkOrderWorkerDTO wowDTO) throws Exception{
		//try {
			wowService.updateWorkOrder(id, wowDTO);
			return new ResponseEntity<>(HttpStatus.OK);
		//} catch (Exception e) {
			// TODO Auto-generated catch block
		//	return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		//}
	}
	
	@DeleteMapping("deleteWorkOrderWorker/{id}")
	public ResponseEntity<?> deleteWow(@PathVariable UUID id){
		wowService.deleteWow(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("updateWorkOrderWorkerBasicInfo/{id}")
	public ResponseEntity<?> updateWorkOrderWorkerBasicInfo(@PathVariable UUID id, @RequestBody WorkOrderWorkerDTO wowDTO) throws Exception{
		//try {
			wowService.updateWOWBasicInfo(id, wowDTO);
			return new ResponseEntity<>(HttpStatus.OK);
		//} catch (Exception e) {
			// TODO Auto-generated catch block
		//	return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		//}
	}
}
