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
	public ResponseEntity<?> addWorker(@PathVariable UUID id,@RequestBody WorkOrderWorkerDTO wowDTO){
		WorkOrder workOrder = workOrderRepo.getOne(id);
		WorkOrderWorker wow = new WorkOrderWorker();
		if(wowDTO.getNightPeriod() != null) {
			wow.setNightPeriod(wowDTO.getNightPeriod());
		} else {
			wow.setNightPeriod(-1.0);
		}
		if(wowDTO.getDayPeriod() != null) {
			wow.setDayPeriod(wowDTO.getDayPeriod());
		} else {
			wow.setDayPeriod(-1.0);
		}
		if(wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
			wow.setWorkPeriod(wowDTO.getNightPeriod() + wowDTO.getDayPeriod());
		} else {
			wow.setWorkPeriod(-1.0);
		}
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		wow.setMachine(machineRepo.getOne(wowDTO.getMachine().getId()));
		wow.setConnectingMachine(machineRepo.getOne(wowDTO.getConnectingMachine().getId()));
		if(wowDTO.getInitialState() != null) {
			wow.setInitialState(wowDTO.getInitialState());
		} else {
			wow.setInitialState(-1.0);
		}
		if(wowDTO.getFinalState() != null) {
			wow.setFinalState(wowDTO.getFinalState());
		} else {
			wow.setFinalState(-1.0);
		}
		if(wowDTO.getInitialState() != null && wowDTO.getFinalState() != null) {
			wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
		} else {
			wow.setSumState(-1.0);
		}
		wow.setWorkOrder(workOrder);
		wow.setUser(userRepo.getOne(wowDTO.getUser().getId()));

		wowRepo.save(wow);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("updateWorkOrderWorker/{id}")
	public ResponseEntity<?> updateWorkOrderWorker(@PathVariable UUID id, @RequestBody WorkOrderWorkerDTO wowDTO){
		WorkOrderWorker wow = wowService.getOne(id);
		if(wowDTO.getDayPeriod() != null) {
			wow.setDayPeriod(wowDTO.getDayPeriod());
		} else {
			wow.setDayPeriod(-1.0);
		}
		if(wowDTO.getNightPeriod() != null) {
			wow.setNightPeriod(wowDTO.getNightPeriod());
		} else {
			wow.setNightPeriod(-1.0);
		}
		if(wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
			wow.setWorkPeriod(wowDTO.getDayPeriod() + wowDTO.getNightPeriod());
		} else {
			wow.setWorkPeriod(-1.0);
		}
		if(wowDTO.getInitialState() != null) {
			wow.setInitialState(wowDTO.getInitialState());
		} else {
			wow.setInitialState(-1.0);
		}
		if(wowDTO.getFinalState() != null) {
			wow.setFinalState(wowDTO.getFinalState());
		} else {
			wow.setFinalState(-1.0);
		}
		if(wowDTO.getFinalState() != null && wowDTO.getInitialState() != null) {
			wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
		} else {
			wow.setSumState(-1.0);
		}
		if(wowDTO.getFuel() != null) {
			wow.setFuel(wowDTO.getFuel());
		} else {
			wow.setFuel(-1.0);
		}
		wow.setUser(userRepo.getOne(wowDTO.getUser().getId()));
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		wow.setConnectingMachine(machineRepo.getOne(wowDTO.getConnectingMachine().getId()));
		wow.setMachine(machineRepo.getOne(wowDTO.getMachine().getId()));
		
		wowRepo.save(wow);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("deleteWorkOrderWorker/{id}")
	public ResponseEntity<?> deleteWow(@PathVariable UUID id){
		wowService.deleteWow(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
